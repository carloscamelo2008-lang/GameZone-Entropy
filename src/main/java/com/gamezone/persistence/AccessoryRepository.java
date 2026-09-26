package com.gamezone.persistence;

import com.gamezone.model.Accessory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of accessories to and from a file, using Java
 * serialization. This class is responsible only for reading and
 * writing data; it contains no business logic.
 *
 * Because Java serialization preserves the concrete runtime type of
 * each object, no explicit discriminator field is needed to tell
 * Controller, Cable and Memory instances apart when loading: each
 * accessory is deserialized back into its original concrete class.
 */
public class AccessoryRepository {

    private String filePath;

    /**
     * Creates a repository backed by the given file path.
     *
     * @param filePath the path of the file used to persist accessories
     */
    public AccessoryRepository(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the complete list of accessories to the file, overwriting
     * any previously stored data.
     *
     * @param accessories the list of accessories to persist
     */
    public void saveAll(List<Accessory> accessories) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(accessories);
        } catch (IOException e) {
            System.out.println("Error saving accessories: " + e.getMessage());
        }
    }

    /**
     * Loads the complete list of accessories from the file. If the file
     * does not exist yet (e.g. on first run), an empty list is returned.
     *
     * @return the list of accessories previously saved, or an empty list
     */
    @SuppressWarnings("unchecked")
    public List<Accessory> loadAll() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Accessory>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
