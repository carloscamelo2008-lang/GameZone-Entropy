package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of accessories to and from a plain-text CSV
 * file. Each line stores a discriminator (CONTROLLER, CABLE or
 * MEMORY) followed by the common fields and the type-specific
 * fields, so the concrete subtype can be reconstructed when loading.
 * This class is responsible only for reading and writing data; it
 * contains no business logic.
 */
public class AccessoryRepository {

    private static final String DELIMITER = ",";
    private static final String CONSOLE_ID_SEPARATOR = ";";

    private String filePath;

    /**
     * Creates a repository backed by the given CSV file path.
     *
     * @param filePath the path of the file used to persist accessories
     */
    public AccessoryRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the complete list of accessories to the CSV file,
     * overwriting any previously stored data.
     *
     * @param accessories the list of accessories to persist
     */
    public void saveAll(List<Accessory> accessories) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Accessory accessory : accessories) {
                String line = toCsvLine(accessory);
                if (line != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving accessories: " + e.getMessage());
        }
    }

    /**
     * Loads the complete list of accessories from the CSV file. If the
     * file does not exist yet (e.g. on first run), an empty list is
     * returned.
     *
     * @return the list of accessories previously saved, or an empty list
     */
    public List<Accessory> loadAll() {
        List<Accessory> accessories = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return accessories;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                Accessory accessory = fromCsvLine(line);
                if (accessory != null) {
                    accessories.add(accessory);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading accessories: " + e.getMessage());
        }

        return accessories;
    }

    /**
     * Converts an accessory into a single CSV line, using a type
     * discriminator so the correct subclass can be rebuilt on load.
     *
     * @param accessory the accessory to convert
     * @return the CSV line, or null if the accessory type is unknown
     */
    private String toCsvLine(Accessory accessory) {
        String type;
        String field1;
        String field2;

        if (accessory instanceof Controller) {
            Controller controller = (Controller) accessory;
            type = "CONTROLLER";
            field1 = controller.getConnectionType();
            field2 = "";
        } else if (accessory instanceof Cable) {
            Cable cable = (Cable) accessory;
            type = "CABLE";
            field1 = String.valueOf(cable.getLengthInMeters());
            field2 = cable.getConnectorType();
        } else if (accessory instanceof Memory) {
            Memory memory = (Memory) accessory;
            type = "MEMORY";
            field1 = String.valueOf(memory.getCapacityInGb());
            field2 = memory.getMemoryType();
        } else {
            return null;
        }

        String compatibleConsoles = String.join(CONSOLE_ID_SEPARATOR, accessory.getCompatibleConsoleIds());

        return String.join(DELIMITER,
                type,
                accessory.getId(),
                accessory.getTitle(),
                String.valueOf(accessory.getPrice()),
                String.valueOf(accessory.getStock()),
                field1,
                field2,
                compatibleConsoles);
    }

    /**
     * Parses a single CSV line back into the correct concrete
     * Accessory subtype, based on its discriminator.
     *
     * @param line the CSV line to parse
     * @return the reconstructed accessory, or null if the line is
     *         malformed or the discriminator is unknown
     */
    private Accessory fromCsvLine(String line) {
        String[] fields = line.split(DELIMITER, -1);
        if (fields.length < 8) {
            return null;
        }

        String type = fields[0];
        String id = fields[1];
        String title = fields[2];
        double price = Double.parseDouble(fields[3]);
        int stock = Integer.parseInt(fields[4]);
        String field1 = fields[5];
        String field2 = fields[6];
        String consolesField = fields[7];

        Accessory accessory;
        switch (type) {
            case "CONTROLLER":
                accessory = new Controller(id, title, price, stock, field1);
                break;
            case "CABLE":
                accessory = new Cable(id, title, price, stock, Double.parseDouble(field1), field2);
                break;
            case "MEMORY":
                accessory = new Memory(id, title, price, stock, Integer.parseInt(field1), field2);
                break;
            default:
                return null;
        }

        if (!consolesField.isEmpty()) {
            for (String consoleId : consolesField.split(CONSOLE_ID_SEPARATOR)) {
                accessory.addCompatibleConsole(consoleId);
            }
        }

        return accessory;
    }
}
