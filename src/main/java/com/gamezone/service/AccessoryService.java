package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;
import com.gamezone.persistence.AccessoryRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides business operations for managing accessories: registration,
 * queries by type and by console compatibility, and stock updates.
 * Delegates persistence to AccessoryRepository.
 */
public class AccessoryService {

    private AccessoryRepository accessoryRepository;
    private List<Accessory> accessories;

    /**
     * Creates the service and loads the currently persisted accessories.
     *
     * @param accessoryRepository the repository used for persistence
     */
    public AccessoryService(AccessoryRepository accessoryRepository) {
        this.accessoryRepository = accessoryRepository;
        this.accessories = accessoryRepository.loadAll();
    }

    /**
     * Registers a new controller and persists the updated list.
     *
     * @param id             the accessory id
     * @param title          the accessory title
     * @param price          the unit price
     * @param stock          the available stock
     * @param connectionType the connection type ("Inalámbrico" or "Alámbrico")
     * @return the newly registered controller
     */
    public Controller registerController(String id, String title, double price, int stock, String connectionType) {
        Controller controller = new Controller(id, title, price, stock, connectionType);
        accessories.add(controller);
        accessoryRepository.saveAll(accessories);
        return controller;
    }

    /**
     * Registers a new cable and persists the updated list.
     *
     * @param id             the accessory id
     * @param title          the accessory title
     * @param price          the unit price
     * @param stock          the available stock
     * @param lengthInMeters the cable length in meters
     * @param connectorType  the connector type
     * @return the newly registered cable
     */
    public Cable registerCable(String id, String title, double price, int stock, double lengthInMeters, String connectorType) {
        Cable cable = new Cable(id, title, price, stock, lengthInMeters, connectorType);
        accessories.add(cable);
        accessoryRepository.saveAll(accessories);
        return cable;
    }

    /**
     * Registers a new memory and persists the updated list.
     *
     * @param id           the accessory id
     * @param title        the accessory title
     * @param price        the unit price
     * @param stock        the available stock
     * @param capacityInGb the storage capacity in gigabytes
     * @param memoryType   the memory type
     * @return the newly registered memory
     */
    public Memory registerMemory(String id, String title, double price, int stock, int capacityInGb, String memoryType) {
        Memory memory = new Memory(id, title, price, stock, capacityInGb, memoryType);
        accessories.add(memory);
        accessoryRepository.saveAll(accessories);
        return memory;
    }

    /**
     * Returns every accessory currently registered.
     *
     * @return the list of all accessories
     */
    public List<Accessory> listAllAccessories() {
        return accessories;
    }

    /**
     * Returns the accessories matching a given type.
     *
     * @param type the simple class name of the type to filter by
     *             ("Controller", "Cable" or "Memory")
     * @return the list of accessories of that type
     */
    public List<Accessory> listAccessoriesByType(String type) {
        List<Accessory> result = new ArrayList<>();
        for (Accessory accessory : accessories) {
            if (accessory.getClass().getSimpleName().equalsIgnoreCase(type)) {
                result.add(accessory);
            }
        }
        return result;
    }

    /**
     * Returns the accessories compatible with a given console.
     *
     * @param consoleId the id of the console to check compatibility against
     * @return the list of compatible accessories
     */
    public List<Accessory> findAccessoriesCompatibleWith(String consoleId) {
        List<Accessory> result = new ArrayList<>();
        for (Accessory accessory : accessories) {
            if (accessory.isCompatibleWith(consoleId)) {
                result.add(accessory);
            }
        }
        return result;
    }

    /**
     * Finds an accessory by its id.
     *
     * @param id the accessory id
     * @return the matching accessory, or null if none is found
     */
    public Accessory findById(String id) {
        for (Accessory accessory : accessories) {
            if (accessory.getId().equals(id)) {
                return accessory;
            }
        }
        return null;
    }

    /**
     * Updates the stock of an accessory by the given quantity and
     * persists the change.
     *
     * @param accessoryId the id of the accessory to update
     * @param quantity    the quantity to add to the current stock
     *                    (negative values decrease it)
     */
    public void updateStock(String accessoryId, int quantity) {
        Accessory accessory = findById(accessoryId);
        if (accessory != null) {
            accessory.setStock(accessory.getStock() + quantity);
            accessoryRepository.saveAll(accessories);
        }
    }
    /**
     * Adds a compatible console to an accessory and persists the change.
     *
     * @param accessoryId the id of the accessory
     * @param consoleId the id of the compatible console
     */
    public void addCompatibleConsole(String accessoryId, String consoleId) {
        Accessory accessory = findById(accessoryId);

        if (accessory == null) {
            throw new IllegalArgumentException(
                    "Accessory not found: " + accessoryId
            );
        }

        if (consoleId == null || consoleId.isBlank()) {
            throw new IllegalArgumentException(
                    "Console ID cannot be empty."
            );
        }

        if (!accessory.getCompatibleConsoleIds().contains(consoleId)) {
            accessory.addCompatibleConsole(consoleId);
            accessoryRepository.saveAll(accessories);
        }
    }

    /**
     * Restores the stock of an accessory after a return, adding the returned
     * quantity to its current stock.
     *
     * @param accessoryId the id of the accessory
     * @param quantity    the number of returned units
     */
    public void restoreStock(String accessoryId, int quantity) {
        updateStock(accessoryId, quantity);
    }
}
