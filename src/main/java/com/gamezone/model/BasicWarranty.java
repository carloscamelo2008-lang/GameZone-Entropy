package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents the basic warranty automatically granted to every
 * console sold by GameZone Unicesar. Covers factory defects for
 * 6 months from the sale date, at no additional cost to the customer.
 */
public class BasicWarranty extends Warranty {

    /**
     * Creates a new basic warranty.
     *
     * @param id        unique identifier of the warranty
     * @param product   product covered by this warranty
     * @param sale      sale in which the product was purchased
     * @param startDate date the warranty coverage begins (the sale date)
     */
    public BasicWarranty(String id, Product product, Sale sale, LocalDate startDate) {
        super(id, product, sale, startDate);
    }

    @Override
    public int getDurationInMonths() {
        return 6;
    }

    @Override
    public String getWarrantyType() {
        return "Garantía Básica";
    }

    @Override
    public double getAdditionalCost() {
        return 0.0;
    }
}
