package com.gamezone.model;

/**
 * Represents a gaming console sold by GameZone Unicesar.
 * In addition to the common product attributes, a console is
 * characterized by its brand, model, and generation.
 */
public class Console extends Product{

    private String brand;
    private String model;
    private String generation;

    /**
     * Creates a new console.
     *
     * @param id         unique identifier of the product
     * @param title      display name of the console
     * @param price      unit price
     * @param stock      available quantity in inventory
     * @param brand      manufacturer brand (e.g. Sony, Microsoft, Nintendo)
     * @param model      model name (e.g. PlayStation 5, Xbox Series X)
     * @param generation console generation (e.g. 9th generation)
     */
    public Console(String id, String title, double price, int stock, String brand, String model, String generation) {
        super(id, title, price, stock);
        this.brand = brand;
        this.model = model;
        this.generation = generation;
    }

    /**
     * Returns the manufacturer brand of this console.
     *
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Updates the manufacturer brand of this console.
     *
     * @param brand the new brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the model name of this console.
     *
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * Updates the model name of this console.
     *
     * @param model the new model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Returns the generation of this console.
     *
     * @return the generation
     */
    public String getGeneration() {
        return generation;
    }

    /**
     * Updates the generation of this console.
     *
     * @param generation the new generation
     */
    public void setGeneration(String generation) {
        this.generation = generation;
    }

    /**
     * Builds a complete description of the console, integrating its
     * brand, model, and generation with the common product information.
     *
     * @return a human-readable description of the console
     */
    @Override
    public String getDescription() {
        return "Console: " + getTitle()
                + " | Brand: " + brand
                + " | Model: " + model
                + " | Generation: " + generation
                + " | Price: " + getPrice()
                + " | Stock: " + getStock();
    }

}
