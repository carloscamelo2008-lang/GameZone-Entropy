package com.gamezone.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a generic accessory sold by GameZone Unicesar.
 * Extends Product to reuse the common attributes and behavior of a
 * sellable product. Serves as the abstract base class for all
 * accessory types, such as controllers, cables, and memory cards.
 */

public abstract class Accessory extends Product{

    private List<String> compatibleConsoleIds;

    /**
     * Creates a new accessory with the given common attributes.
     * The list of compatible consoles starts empty.
     *
     * @param id    unique identifier of the accessory
     * @param title display name of the accessory
     * @param price unit price of the accessory
     * @param stock available quantity in inventory
     */

    public Accessory(String id,String title,double price, int stock){
        super(id,title,price,stock);
        this.compatibleConsoleIds = new ArrayList<>();
    }

    /**
     * Returns the list of console ids this accessory is compatible with.
     *
     * @return the list of compatible console ids
     */

    public List<String> getCompatibleConsoleIds() {
        return compatibleConsoleIds;
    }

    /**
     * Returns the list of console ids this accessory is compatible with.
     *
     * @return the list of compatible console ids
     */

    public void addCompatibleConsole(String consoleId) {
        compatibleConsoleIds.add(consoleId);
    }

    /**
     * Removes a console from the list of compatible consoles.
     *
     * @param consoleId the id of the console to remove
     */

    public void removeCompatibleConsole(String consoleId) {
        compatibleConsoleIds.remove(consoleId);
    }

    /**
     * Checks whether this accessory is compatible with the given console.
     *
     * @param consoleId the id of the console to check
     * @return true if the console id is in the compatibility list
     */

    public boolean isCompatibleWith(String consoleId) {
        return compatibleConsoleIds.contains(consoleId);
    }

    /**
     * Builds a description of the accessory that integrates the
     * common product information with its console compatibility list.
     * Subclasses extend this description with their own specific
     * characteristics by calling {@code super.getDescription()}.
     *
     * @return a human-readable description of the accessory
     */

    @Override
    public String getDescription(){
        return "Accessory: " + getTitle()
                + " | Price: " + getPrice()
                + " | Stock: " + getStock()
                + " | Compatible with: " + compatibleConsoleIds;
    }
}
