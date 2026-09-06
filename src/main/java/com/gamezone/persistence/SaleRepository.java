package com.gamezone.persistence;

import com.gamezone.model.Sale;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of sales to and from a file using Java serialization.
 * This class is responsible only for reading and writing sale data.
 */
public class SaleRepository {

    private final String filePath;

    /**
     * Creates a new sale repository.
     *
     * @param filePath path of the file used to store sales
     */
    public SaleRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the complete list of sales to the file.
     *
     * @param sales the list of sales to persist
     */
    public void saveAll(List<Sale> sales) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(sales);
        } catch (IOException e) {
            System.out.println("Error saving sales: " + e.getMessage());
        }
    }

    /**
     * Loads the complete list of sales from the file.
     *
     * @return the list of previously saved sales, or an empty list if the file
     * does not exist or cannot be read
     */
    @SuppressWarnings("unchecked")
    public List<Sale> loadAll() {
        File file = new File(filePath);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Sale>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}