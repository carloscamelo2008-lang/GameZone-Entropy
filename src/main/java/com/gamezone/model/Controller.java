package com.gamezone.model;

/**
 * Represents a game controller accessory sold by GameZone Unicesar.
 * In addition to the common accessory attributes, a controller is
 * characterized by its connection type.
 */

public class Controller extends Accessory{
    private String connectionType;

    /**
     * Creates a new controller.
     *
     * @param id             unique identifier of the accessory
     * @param title          display name of the controller
     * @param price          unit price
     * @param stock          available quantity in inventory
     * @param connectionType type of connection (e.g. wireless, wired)
     */
    public Controller(String id, String title, double price, int stock, String connectionType) {
        super(id, title, price, stock);
        this.connectionType = connectionType;
    }

    /**
     * Returns the connection type of this controller.
     *
     * @return the connection type
     */
    public String getConnectionType() {
        return connectionType;
    }

    /**
     * Updates the connection type of this controller.
     *
     * @param connectionType the new connection type
     */
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    /**
     * Builds a complete description of the controller, extending the
     * accessory description with its connection type.
     *
     * @return a human-readable description of the controller
     */
    @Override
    public String getDescription() {
        return super.getDescription() + " | Connection Type: " + connectionType;
    }
}
