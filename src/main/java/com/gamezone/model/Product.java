package com.gamezone.model;

import java.io.Serializable;



/**
 * Represents a generic product sold by GameZone Unicesar.
 * Serves as the abstract base class for all product types
 * commercialized by the store, such as video games and consoles.
 */

public abstract class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private double price;
    private int stock;

    /**
     * Represents a generic product sold by GameZone Unicesar.
     * Serves as the abstract base class for all product types
     * commercialized by the store, such as video games and consoles.
     */

    public Product(String id, String title, double price, int stock) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Returns the unique identifier of the product.
     *
     * @return the product id
     */

    public String getId() {
        return id;
    }

    /**
     * Updates the unique identifier of the product.
     *
     * @param id the new product id
     */

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the display title of the product.
     *
     * @return the product title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the display title of the product.
     *
     * @return the product title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the unit price of the product.
     *
     * @return the product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Updates the unit price of the product.
     *
     * @param price the new product price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns the quantity of this product available in inventory.
     *
     * @return the available stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Updates the quantity of this product available in inventory.
     *
     * @param stock the new stock quantity
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Builds a complete description of the product, integrating the
     * particular characteristics of each specific product type.
     * Each subclass must provide its own implementation.
     *
     * @return a human-readable description of the product
     */
    public abstract String getDescription();
}
