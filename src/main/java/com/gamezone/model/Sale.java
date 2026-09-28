package com.gamezone.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a sale made in GameZone Unicesar.
 * A sale is associated with a customer, a seller, a date,
 * and one or more products.
 */
public class Sale implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDateTime date;
    private Customer customer;
    private Seller seller;
    private List<Product> products;
    private String appliedPromotionName;
    private double discountAmount;
    private double extendedWarrantyCost;

    /**
     * Creates a new sale.
     *
     * @param date the date and time of the sale
     * @param customer the customer who made the purchase
     * @param seller the seller who handled the sale
     * @param products the products included in the sale
     */
    public Sale(LocalDateTime date, Customer customer, Seller seller, List<Product> products) {
        this.date = date;
        this.customer = customer;
        this.seller = seller;
        this.products = products;
        this.appliedPromotionName = null;
        this.discountAmount = 0.0;
        this.extendedWarrantyCost = 0.0;
    }

    /**
     * Returns the date and time of the sale.
     *
     * @return the sale date and time
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Returns the customer associated with the sale.
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns the seller associated with the sale.
     *
     * @return the seller
     */
    public Seller getSeller() {
        return seller;
    }

    /**
     * Returns the products included in the sale.
     *
     * @return the list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Returns the name of the promotion applied to the sale.
     *
     * @return the applied promotion name, or null if no promotion was applied
     */
    public String getAppliedPromotionName() {
        return appliedPromotionName;
    }

    /**
     * Updates the name of the promotion applied to the sale.
     *
     * @param appliedPromotionName the applied promotion name
     */
    public void setAppliedPromotionName(String appliedPromotionName) {
        this.appliedPromotionName = appliedPromotionName;
    }

    /**
     * Returns the monetary discount applied to the sale.
     *
     * @return the discount amount
     */
    public double getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Updates the monetary discount applied to the sale.
     *
     * @param discountAmount the discount amount
     */
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     * Returns the additional cost of extended warranties.
     *
     * @return the extended warranty cost
     */
    public double getExtendedWarrantyCost() {
        return extendedWarrantyCost;
    }

    /**
     * Updates the additional cost of extended warranties.
     *
     * @param extendedWarrantyCost the extended warranty cost
     */
    public void setExtendedWarrantyCost(double extendedWarrantyCost) {
        this.extendedWarrantyCost = extendedWarrantyCost;
    }

    /**
     * Calculates the subtotal of the sale before discounts and warranties.
     *
     * @return the subtotal of all products
     */
    public double calculateTotal() {
        double total = 0;

        for (Product product : products) {
            total += product.getPrice();
        }

        return total;
    }

    /**
     * Calculates the final total after applying the discount
     * and adding the extended warranty cost.
     *
     * @return the final total of the sale
     */
    public double calculateFinalTotal() {
        return calculateTotal() - discountAmount + extendedWarrantyCost;
    }

    /**
     * Generates a formatted receipt for the sale.
     *
     * @return the sale receipt with subtotal, discount,
     *         extended warranty cost and final total
     */
    public String generateReceipt() {
        String promotion = appliedPromotionName == null
                ? "Sin promoción"
                : appliedPromotionName;

        return "===== RECIBO DE VENTA =====\n"
                + "Subtotal: $" + calculateTotal() + "\n"
                + "Descuento (" + promotion + "): -$" + discountAmount + "\n"
                + "Garantías extendidas: +$" + extendedWarrantyCost + "\n"
                + "Total final: $" + calculateFinalTotal();
    }
    /**
     * Checks whether the sale is eligible for a return. A sale is eligible
     * only if it was made between 0 and 30 days ago; sales with a future
     * date are not eligible.
     *
     * @return true if the sale is between 0 and 30 days old
     */
    public boolean canBeReturned() {
        long daysSinceSale = java.time.temporal.ChronoUnit.DAYS.between(
                getDate().toLocalDate(), java.time.LocalDate.now());
        return daysSinceSale >= 0 && daysSinceSale <= 30;
    }
}