package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a promotion that applies a fixed percentage discount
 * over the total value of a sale.
 */
public class PercentageDiscount extends Promotion {

    private double percentage;

    /**
     * Creates a new percentage-based promotion.
     *
     * @param id         unique identifier of the promotion
     * @param name       display name of the promotion
     * @param startDate  date the promotion becomes active
     * @param endDate    date the promotion stops being active
     * @param percentage discount percentage to apply (0 to 100)
     */
    public PercentageDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                              double percentage) {
        super(id, name, startDate, endDate);
        this.percentage = percentage;
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
     * Calculates the discount by applying the percentage to the
     * total value of the sale.
     *
     * @param sale the sale to evaluate
     * @return the discount amount in currency
     */
    @Override
    public double calculateDiscount(Sale sale) {
        return sale.calculateTotal() * (percentage / 100);
    }
}