package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a promotion that applies a percentage discount over the
 * total value of a sale, only when the sale includes at least a
 * minimum quantity of products.
 */
public class BulkPurchaseDiscount extends Promotion {

    private int minimumQuantity;
    private double percentage;

    /**
     * Creates a new bulk-purchase promotion.
     *
     * @param id              unique identifier of the promotion
     * @param name            display name of the promotion
     * @param startDate       date the promotion becomes active
     * @param endDate         date the promotion stops being active
     * @param minimumQuantity minimum number of products required
     * @param percentage      discount percentage to apply (0 to 100)
     */
    public BulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                                int minimumQuantity, double percentage) {
        super(id, name, startDate, endDate);
        this.minimumQuantity = minimumQuantity;
        this.percentage = percentage;
    }

    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Calculates the discount by applying the percentage to the total
     * value of the sale, but only if the sale includes at least the
     * minimum required quantity of products.
     *
     * @param sale the sale to evaluate
     * @return the discount amount in currency, or zero if the minimum
     *         quantity is not met
     */
    @Override
    public double calculateDiscount(Sale sale) {
        if (sale.getProducts().size() >= minimumQuantity) {
            return sale.calculateTotal() * (percentage / 100);
        }
        return 0;
    }
}