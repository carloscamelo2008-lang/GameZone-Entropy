package com.gamezone.persistence;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of promotions to and from a plain-text CSV
 * file. Each line stores a discriminator (PERCENTAGE, CATEGORY or
 * BULK) followed by the common fields and the type-specific fields,
 * so the concrete subtype can be reconstructed when loading. Text
 * fields containing commas or quotes are wrapped in double quotes,
 * with inner quotes doubled, so they can be loaded back safely. This
 * class is responsible only for reading and writing data; it
 * contains no business logic.
 */
public class PromotionRepository {

    private static final String DELIMITER = ",";

    private String filePath;

    /**
     * Creates a repository backed by the given CSV file path.
     *
     * @param filePath the path of the file used to persist promotions
     */
    public PromotionRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the complete list of promotions to the CSV file,
     * overwriting any previously stored data.
     *
     * @param promotions the list of promotions to persist
     */
    public void saveAll(List<Promotion> promotions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Promotion promotion : promotions) {
                String line = toCsvLine(promotion);
                if (line != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving promotions: " + e.getMessage());
        }
    }

    /**
     * Loads the complete list of promotions from the CSV file. If the
     * file does not exist yet (e.g. on first run), an empty list is
     * returned.
     *
     * @return the list of promotions previously saved, or an empty list
     */
    public List<Promotion> loadAll() {
        List<Promotion> promotions = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return promotions;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                Promotion promotion = fromCsvLine(line);
                if (promotion != null) {
                    promotions.add(promotion);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading promotions: " + e.getMessage());
        }

        return promotions;
    }

    /**
     * Converts a promotion into a single CSV line, using a type
     * discriminator so the correct subclass can be rebuilt on load.
     * Text fields are escaped so commas and quotes are preserved.
     *
     * @param promotion the promotion to convert
     * @return the CSV line, or null if the promotion type is unknown
     */
    private String toCsvLine(Promotion promotion) {
        String type;
        String field1;
        String field2;

        if (promotion instanceof PercentageDiscount) {
            PercentageDiscount percentageDiscount = (PercentageDiscount) promotion;
            type = "PERCENTAGE";
            field1 = String.valueOf(percentageDiscount.getPercentage());
            field2 = "";
        } else if (promotion instanceof CategoryDiscount) {
            CategoryDiscount categoryDiscount = (CategoryDiscount) promotion;
            type = "CATEGORY";
            field1 = String.valueOf(categoryDiscount.getPercentage());
            field2 = categoryDiscount.getTargetCategory();
        } else if (promotion instanceof BulkPurchaseDiscount) {
            BulkPurchaseDiscount bulkPurchaseDiscount = (BulkPurchaseDiscount) promotion;
            type = "BULK";
            field1 = String.valueOf(bulkPurchaseDiscount.getMinimumQuantity());
            field2 = String.valueOf(bulkPurchaseDiscount.getPercentage());
        } else {
            return null;
        }

        return String.join(DELIMITER,
                type,
                escapeField(promotion.getId()),
                escapeField(promotion.getName()),
                promotion.getStartDate().toString(),
                promotion.getEndDate().toString(),
                field1,
                escapeField(field2));
    }

    /**
     * Parses a single CSV line back into the correct concrete
     * Promotion subtype, based on its discriminator.
     *
     * @param line the CSV line to parse
     * @return the reconstructed promotion, or null if the line is
     *         malformed or the discriminator is unknown
     */
    private Promotion fromCsvLine(String line) {
        List<String> fields = splitCsvLine(line);
        if (fields.size() < 7) {
            return null;
        }

        String type = fields.get(0);
        String id = fields.get(1);
        String name = fields.get(2);
        String field1 = fields.get(5);
        String field2 = fields.get(6);

        try {
            LocalDate startDate = LocalDate.parse(fields.get(3));
            LocalDate endDate = LocalDate.parse(fields.get(4));

            switch (type) {
                case "PERCENTAGE":
                    return new PercentageDiscount(id, name, startDate, endDate, Double.parseDouble(field1));
                case "CATEGORY":
                    return new CategoryDiscount(id, name, startDate, endDate, Double.parseDouble(field1), field2);
                case "BULK":
                    return new BulkPurchaseDiscount(id, name, startDate, endDate,
                            Integer.parseInt(field1), Double.parseDouble(field2));
                default:
                    return null;
            }
        } catch (DateTimeParseException | NumberFormatException e) {
            return null;
        }
    }

    /**
     * Escapes a value so it can be stored safely in a CSV field.
     * Values containing the delimiter or quotes are wrapped in double
     * quotes, and inner quotes are doubled. Line breaks are replaced
     * by spaces so each promotion stays on a single line.
     *
     * @param value the raw field value
     * @return the escaped field value
     */
    private String escapeField(String value) {
        if (value == null) {
            return "";
        }
        String clean = value.replace("\r", " ").replace("\n", " ");
        if (clean.contains(DELIMITER) || clean.contains("\"")) {
            return "\"" + clean.replace("\"", "\"\"") + "\"";
        }
        return clean;
    }

    /**
     * Splits a CSV line into fields, honoring double-quoted fields that
     * may contain the delimiter and doubled quotes.
     *
     * @param line the CSV line to split
     * @return the list of unescaped field values
     */
    private List<String> splitCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else if (c == '"') {
                inQuotes = true;
            } else if (c == DELIMITER.charAt(0)) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields;
    }
}