package com.gamezone.model;

/**
 * Represents a memory storage accessory sold by GameZone Unicesar.
 * In addition to the common accessory attributes, a memory is
 * characterized by its storage capacity in gigabytes and its type.
 */
public class Memory extends Accessory {

    private static final long serialVersionUID = 1L;

    private int capacityInGigabytes;
    private String memoryType;

    /**
     * Creates a new memory.
     *
     * @param id                  unique identifier of the accessory
     * @param title               display name of the memory
     * @param price               unit price
     * @param stock               available quantity in inventory
     * @param capacityInGigabytes storage capacity, in gigabytes
     * @param memoryType          memory type (e.g. "SD", "microSD", "Tarjeta interna")
     */
    public Memory(String id, String title, double price, int stock, int capacityInGigabytes, String memoryType) {
        super(id, title, price, stock);
        this.capacityInGigabytes = capacityInGigabytes;
        this.memoryType = memoryType;
    }

    /**
     * Returns the storage capacity of this memory, in gigabytes.
     *
     * @return the capacity in gigabytes
     */
    public int getCapacityInGigabytes() {
        return capacityInGigabytes;
    }

    /**
     * Updates the storage capacity of this memory, in gigabytes.
     *
     * @param capacityInGigabytes the new capacity in gigabytes
     */
    public void setCapacityInGigabytes(int capacityInGigabytes) {
        this.capacityInGigabytes = capacityInGigabytes;
    }

    /**
     * Returns the type of this memory.
     *
     * @return the memory type
     */
    public String getMemoryType() {
        return memoryType;
    }

    /**
     * Updates the type of this memory.
     *
     * @param memoryType the new memory type
     */
    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    /**
     * Builds a complete description of the memory, integrating its
     * capacity and type with the common accessory information.
     *
     * @return a human-readable description of the memory
     */
    @Override
    public String getDescription() {
        return "Memory: " + getTitle()
                + " | Capacity (GB): " + capacityInGigabytes
                + " | Memory type: " + memoryType
                + " | Price: " + getPrice()
                + " | Stock: " + getStock()
                + " | Compatible consoles: " + getCompatibleConsoleIds();
    }
}
