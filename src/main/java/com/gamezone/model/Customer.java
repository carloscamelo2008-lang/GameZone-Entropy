package com.gamezone.model;

/**
 * Represents a customer of the store. A customer is a person who buys
 * products; their purchase history is tracked through the Sale module,
 * not stored redundantly here, to avoid duplicating data across layers.
 */
public class Customer extends Person {

    private String email;

    /**
     * Creates a new Customer.
     *
     * @param id    unique identification of the customer
     * @param name  full name of the customer
     * @param phone contact phone number
     * @param email email address of the customer
     */
    public Customer(String id, String name, String phone, String email) {
        super(id, name, phone);
        this.email = email;
    }

    /**
 * Returns the email address of this customer.
 *
 * @return the customer's email
 */
    public String getEmail() {
        return email;
    }

    /**
 * Updates the email address of this customer.
 *
 * @param email the new email
 */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns a description of this customer's role-specific information.
     *
     * @return a string describing the customer's email
     */
    @Override
    public String getRoleDescription() {
        return "Customer - Email: " + email;
    }
}
