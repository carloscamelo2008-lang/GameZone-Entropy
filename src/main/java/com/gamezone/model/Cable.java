package com.gamezone.model;

/**
 * Represents a cable accessory sold by GameZone Unicesar.
 * In addition to the common accessory attributes, a cable is
 * characterized by its length in meters and its connector type.
 */
public class Cable extends Accessory {

    private static final long serialVersionUID = 1L;

    private double lengthInMeters;
    private String connectorType;

    /**
     * Creates a new cable.
     *
     * @param id             unique identifier of the accessory
     * @param title          display name of the cable
     * @param price          unit price
     * @param stock          available quantity in inventory
     * @param lengthInMeters length of the cable, in meters
     * @param connectorType  connector type (e.g. "HDMI", "USB", "Óptico")
     */
    public Cable(String id, String title, double price, int stock, double lengthInMeters, String connectorType) {
        super(id, title, price, stock);
        this.lengthInMeters = lengthInMeters;
        this.connectorType = connectorType;
    }

    /**
     * Returns the length of this cable, in meters.
     *
     * @return the length in meters
     */
    public double getLengthInMeters() {
        return lengthInMeters;
    }

    /**
     * Updates the length of this cable, in meters.
     *
     * @param lengthInMeters the new length in meters
     */
    public void setLengthInMeters(double lengthInMeters) {
        this.lengthInMeters = lengthInMeters;
    }

    /**
     * Returns the connector type of this cable.
     *
     * @return the connector type
     */
    public String getConnectorType() {
        return connectorType;
    }

    /**
     * Updates the connector type of this cable.
     *
     * @param connectorType the new connector type
     */
    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    /**
     * Builds a complete description of the cable, integrating its
     * length and connector type with the common accessory information.
     *
     * @return a human-readable description of the cable
     */
    @Override
    public String getDescription() {
        return "Cable: " + getTitle()
                + " | Length (m): " + lengthInMeters
                + " | Connector type: " + connectorType
                + " | Price: " + getPrice()
                + " | Stock: " + getStock()
                + " | Compatible consoles: " + getCompatibleConsoleIds();
    }
}