package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents the extended warranty optionally offered by GameZone
 * Unicesar. Covers factory defects and accidental damage for 12
 * months from the sale date, at a cost of 10% of the covered
 * product's price.
 */
public class ExtendedWarranty extends Warranty {

    private static final double COST_PERCENTAGE = 0.10;

    /**
     * Creates a new extended warranty.
     *
     * @param id        unique identifier of the warranty
     * @param product   product covered by this warranty
     * @param sale      sale in which the product was purchased
     * @param startDate date the warranty coverage begins (the sale date)
     */
    public ExtendedWarranty(String id, Product product, Sale sale, LocalDate startDate) {
        super(id, product, sale, startDate);
    }

    @Override
    public int getDurationInMonths() {
        return 12;
    }

    @Override
    public String getWarrantyType() {
        return "Garantía Extendida";
    }

    @Override
    public double getAdditionalCost() {
        return getProduct().getPrice() * COST_PERCENTAGE;
    }
}
