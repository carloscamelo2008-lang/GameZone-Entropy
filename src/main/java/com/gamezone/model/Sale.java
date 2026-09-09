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
     * Calculates the total value of the sale.
     *
     * @return the total price of all products
     */
    public double calculateTotal() {
        double total = 0;

        for (Product product : products) {
            total += product.getPrice();
        }

        return total;
    }
}