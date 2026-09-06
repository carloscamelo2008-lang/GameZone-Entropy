package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading Customer and Seller data from CSV files.
 * This class belongs to the persistence layer: it is the only place
 * where file access logic for the person module lives, keeping the
 * model classes free of storage concerns.
 */
public class PersonRepository {

    private final String customersFilePath;
    private final String sellersFilePath;

    /**
     * Creates a repository configured with the file paths to use for
     * customers and sellers data.
     *
     * @param customersFilePath path to the customers data file
     * @param sellersFilePath   path to the sellers data file
     */
    public PersonRepository(String customersFilePath, String sellersFilePath) {
        this.customersFilePath = customersFilePath;
        this.sellersFilePath = sellersFilePath;
    }

    /**
     * Saves the given list of customers to the customers data file,
     * overwriting any previous content.
     *
     * @param customers the list of customers to persist
     */
    public void saveCustomers(List<Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(customersFilePath))) {
            for (Customer c : customers) {
                writer.println(String.join(",", c.getId(), c.getName(), c.getPhone(), c.getEmail()));
            }
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    /**
     * Loads the list of customers from the customers data file.
     * Returns an empty list if the file does not exist yet.
     *
     * @return the list of customers read from the file
     */
    public List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(customersFilePath);
        if (!file.exists()) {
            return customers;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", -1);
                customers.add(new Customer(parts[0], parts[1], parts[2], parts[3]));
            }
        } catch (IOException e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
        return customers;
    }

    /**
     * Saves the given list of sellers to the sellers data file,
     * overwriting any previous content.
     *
     * @param sellers the list of sellers to persist
     */
    public void saveSellers(List<Seller> sellers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(sellersFilePath))) {
            for (Seller s : sellers) {
                writer.println(String.join(",", s.getId(), s.getName(), s.getPhone(),
                        s.getEmployeeCode(), s.getShift()));
            }
        } catch (IOException e) {
            System.out.println("Error saving sellers: " + e.getMessage());
        }
    }

    /**
     * Loads the list of sellers from the sellers data file.
     * Returns an empty list if the file does not exist yet.
     *
     * @return the list of sellers read from the file
     */
    public List<Seller> loadSellers() {
        List<Seller> sellers = new ArrayList<>();
        File file = new File(sellersFilePath);
        if (!file.exists()) {
            return sellers;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", -1);
                sellers.add(new Seller(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
        } catch (IOException e) {
            System.out.println("Error loading sellers: " + e.getMessage());
        }
        return sellers;
    }
}
