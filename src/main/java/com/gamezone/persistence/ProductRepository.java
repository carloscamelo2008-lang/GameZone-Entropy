package com.gamezone.persistence;

import com.gamezone.model.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of products to and from a file, using Java
 * serialization. This class is responsible only for reading and
 * writing data; it contains no business logic.
 */

public class ProductRepository {

    private String filePath;

    /**
     * Handles persistence of products to and from a file, using Java
     * serialization. This class is responsible only for reading and
     * writing data; it contains no business logic.
     */
    public ProductRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the complete list of products to the file, overwriting
     * any previously stored data.
     *
     * @param products the list of products to persist
     */
    public void saveAll(List<Product> products) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(products);
        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }

    /**
     * Loads the complete list of products from the file. If the file
     * does not exist yet (e.g. on first run), an empty list is returned.
     *
     * @return the list of products previously saved, or an empty list
     */
    public List<Product> loadAll(){
        File file = new File(filePath);
        if (!file.exists()){
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))){
            return (List<Product>) ois.readObject();
        } catch (IOException | ClassNotFoundException e){
            return new ArrayList<>();
        }
    }

}