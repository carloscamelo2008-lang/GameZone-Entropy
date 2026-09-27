package com.gamezone.persistence;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.Warranty;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of warranties to and from a CSV file. This class
 * is responsible only for reading and writing raw data; it does not
 * resolve Sale or Product references and has no dependency on any
 * service. Resolving those references is the responsibility of
 * {@link com.gamezone.service.WarrantyService}, which avoids the
 * circular dependency that would otherwise arise if this repository
 * depended on SaleService.
 */
public class WarrantyRepository {

    private final String filePath;

    /**
     * Creates a warranty repository backed by the given CSV file.
     *
     * @param filePath path to the CSV file used for persistence
     */
    public WarrantyRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the complete list of warranties to the file, overwriting
     * any previously stored data. Each warranty is expected to already
     * carry its resolved Product and Sale references.
     *
     * @param warranties the list of warranties to persist
     */
    public void saveAll(List<Warranty> warranties) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Warranty warranty : warranties) {
                writer.println(toCsvLine(warranty));
            }
        } catch (IOException e) {
            System.out.println("Error saving warranties: " + e.getMessage());
        }
    }

    /**
     * Loads the complete list of warranty records from the file. If the
     * file does not exist yet (e.g. on first run), an empty list is
     * returned. Each record carries only raw identifiers; resolving
     * them into real Product and Sale references is done by the caller.
     *
     * @return the list of raw warranty records previously saved, or an
     *         empty list
     */
    public List<WarrantyRecord> loadAll() {
        List<WarrantyRecord> records = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                records.add(fromCsvLine(line));
            }
        } catch (IOException e) {
            System.out.println("Error loading warranties: " + e.getMessage());
        }

        return records;
    }

    /**
     * Converts a resolved warranty into a single CSV line, using a
     * discriminator column to distinguish between the two concrete
     * warranty types.
     *
     * @param warranty the warranty to serialize
     * @return the CSV representation of the warranty
     */
    private String toCsvLine(Warranty warranty) {
        String type = (warranty instanceof BasicWarranty) ? "BASIC" : "EXTENDED";
        String saleReference = warranty.getSale().getDate() + "|" + warranty.getSale().getCustomer().getId();

        return warranty.getId() + ","
                + type + ","
                + warranty.getProduct().getId() + ","
                + saleReference + ","
                + warranty.getStartDate();
    }

    /**
     * Parses a single CSV line into a raw warranty record, without
     * resolving any reference.
     *
     * @param line the CSV line to parse
     * @return the parsed raw warranty record
     */
    private WarrantyRecord fromCsvLine(String line) {
        String[] fields = line.split(",", -1);

        String id = fields[0];
        String type = fields[1];
        String productId = fields[2];
        String saleReference = fields[3];
        LocalDate startDate = LocalDate.parse(fields[4]);

        return new WarrantyRecord(id, type, productId, saleReference, startDate);
    }

    /**
     * Raw data read from a single line of the warranty CSV file, before
     * any Sale or Product reference has been resolved.
     */
    public static class WarrantyRecord {
        private final String id;
        private final String type;
        private final String productId;
        private final String saleReference;
        private final LocalDate startDate;

        /**
         * Creates a raw warranty record.
         *
         * @param id            the warranty id
         * @param type          the discriminator ("BASIC" or "EXTENDED")
         * @param productId     the id of the covered product
         * @param saleReference the composite reference of the associated sale
         * @param startDate     the date the warranty coverage begins
         */
        public WarrantyRecord(String id, String type, String productId, String saleReference, LocalDate startDate) {
            this.id = id;
            this.type = type;
            this.productId = productId;
            this.saleReference = saleReference;
            this.startDate = startDate;
        }

        /**
         * Returns the warranty id.
         *
         * @return the warranty id
         */
        public String getId() {
            return id;
        }

        /**
         * Returns the type discriminator ("BASIC" or "EXTENDED").
         *
         * @return the type discriminator
         */
        public String getType() {
            return type;
        }

        /**
         * Returns the id of the covered product.
         *
         * @return the product id
         */
        public String getProductId() {
            return productId;
        }

        /**
         * Returns the composite reference of the associated sale.
         *
         * @return the sale reference
         */
        public String getSaleReference() {
            return saleReference;
        }

        /**
         * Returns the date the warranty coverage begins.
         *
         * @return the start date
         */
        public LocalDate getStartDate() {
            return startDate;
        }
    }
}
