package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a promotion that applies a percentage discount only to
 * the products of a specific category within a sale.
 */
public class CategoryDiscount extends Promotion {

    private double percentage;
    private String targetCategory;

    /**
     * Creates a new category-based promotion.
     *
     * @param id             unique identifier of the promotion
     * @param name           display name of the promotion
     * @param startDate      date the promotion becomes active
     * @param endDate        date the promotion stops being active
     * @param percentage     discount percentage to apply (0 to 100)
     * @param targetCategory category the discount applies to
     *                       ("VIDEOGAME", "CONSOLE", or "ACCESSORY")
     */
    public CategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                            double percentage, String targetCategory) {
        super(id, name, startDate, endDate);
        this.percentage = percentage;
        this.targetCategory = targetCategory;
    }

    /**
     * Returns the discount percentage of this promotion.
     *
     * @return the discount percentage
     */
    public double getPercentage() {
        return percentage;
    }

    /**
     * Updates the discount percentage of this promotion.
     *
     * @param percentage the new discount percentage
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Returns the category this promotion applies its discount to.
     *
     * @return the target category
     */
    public String getTargetCategory() {
        return targetCategory;
    }

    /**
     * Updates the category this promotion applies its discount to.
     *
     * @param targetCategory the new target category
     */
    public void setTargetCategory(String targetCategory) {
        this.targetCategory = targetCategory;
    }

    /**
     * Calculates the discount by applying the percentage only to the
     * products in the sale that belong to the target category.
     *
     * @param sale the sale to evaluate
     * @return the discount amount in currency
     */
    @Override
    public double calculateDiscount(Sale sale) {
        double categoryTotal = 0;

        for (Product product : sale.getProducts()) {
            if (matchesTargetCategory(product)) {
                categoryTotal += product.getPrice();
            }
        }

        return categoryTotal * (percentage / 100);
    }

    /**
     * Determines whether the given product belongs to this promotion's
     * target category.
     * <p>
     * Video games and consoles are matched by their concrete type.
     * Accessories do not yet have a dedicated product type in this
     * module (they are provided by a separate integration currently
     * in development), so any product that is neither a video game
     * nor a console is treated as an accessory. Once a dedicated
     * {@code Accessory} product type is integrated, this branch
     * should be replaced with an explicit {@code instanceof Accessory}
     * check.
     *
     * @param product the product to classify
     * @return true if the product belongs to the target category
     */
    private boolean matchesTargetCategory(Product product) {
        if (targetCategory.equalsIgnoreCase("VIDEOGAME")) {
            return product instanceof VideoGame;
        }
        if (targetCategory.equalsIgnoreCase("CONSOLE")) {
            return product instanceof Console;
        }
        if (targetCategory.equalsIgnoreCase("ACCESSORY")) {
            return !(product instanceof VideoGame) && !(product instanceof Console);
        }
        return false;
    }
}