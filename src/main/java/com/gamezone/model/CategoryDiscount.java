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
     *                       ("VIDEOGAME" or "CONSOLE")
     */
    public CategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                            double percentage, String targetCategory) {
        super(id, name, startDate, endDate);
        this.percentage = percentage;
        this.targetCategory = targetCategory;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getTargetCategory() {
        return targetCategory;
    }

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
            if (targetCategory.equals("VIDEOGAME") && product instanceof VideoGame) {
                categoryTotal += product.getPrice();
            } else if (targetCategory.equals("CONSOLE") && product instanceof Console) {
                categoryTotal += product.getPrice();
            }
        }

        return categoryTotal * (percentage / 100);
    }
}
