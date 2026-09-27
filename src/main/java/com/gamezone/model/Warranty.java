package com.gamezone.model;

import java.time.LocalDate;

/**
 * Represents a warranty coverage granted to a product sold by
 * GameZone Unicesar. Serves as the abstract base class for all
 * warranty types, each with its own duration, coverage type, and
 * additional cost.
 */
public abstract class Warranty {

    private String id;
    private Product product;
    private Sale sale;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a new warranty. The end date is calculated automatically
     * from the start date, using the duration provided by the concrete
     * subclass through {@link #getDurationInMonths()}.
     *
     * @param id        unique identifier of the warranty
     * @param product   product covered by this warranty
     * @param sale      sale in which the product was purchased
     * @param startDate date the warranty coverage begins (the sale date)
     */
    public Warranty(String id, Product product, Sale sale, LocalDate startDate) {
        this.id = id;
        this.product = product;
        this.sale = sale;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(getDurationInMonths());
    }

    /**
     * Returns the unique identifier of the warranty.
     *
     * @return the warranty id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the product covered by this warranty.
     *
     * @return the covered product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Returns the sale in which the covered product was purchased.
     *
     * @return the associated sale
     */
    public Sale getSale() {
        return sale;
    }

    /**
     * Returns the date the warranty coverage begins.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the date the warranty coverage ends.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Returns the coverage duration of this warranty type, in months.
     * Each concrete subclass defines its own duration, without
     * duplicating the end-date calculation logic declared in the
     * constructor of this base class.
     */
    public abstract int getDurationInMonths();

    /**
     * Returns the display name of this warranty type.
     */
    public abstract String getWarrantyType();

    /**
     * Returns the additional cost that this warranty adds to the sale total.
     */
    public abstract double getAdditionalCost();

    /**
     * Checks whether this warranty is active on the given date.
     */
    public boolean isActive(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Builds a formatted, human-readable certificate with the details
     * of this warranty.
     */
    public String generateWarrantyCertificate() {
        return "Certificado de garantía"
                + " | Identificador: " + id
                + " | Tipo: " + getWarrantyType()
                + " | Producto: " + product.getTitle()
                + " | Fecha de inicio: " + startDate
                + " | Fecha de fin: " + endDate
                + " | Costo adicional: " + getAdditionalCost();
    }
}