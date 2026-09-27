package com.gamezone.model;

import java.time.LocalDate;
/**
 * Represents a generic promotional discount campaign offered by
 * GameZone Unicesar. Serves as the abstract base class for all
 * promotion types, each with its own discount calculation strategy.
 */
public abstract class Promotion {

    private String id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Represents a generic promotional discount campaign offered by
     * GameZone Unicesar. Serves as the abstract base class for all
     * promotion types, each with its own discount calculation strategy.
     */

    public Promotion(String id, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the unique identifier of the promotion.
     *
     * @return the promotion id
     */
    public String getId() {
        return id;
    }

    /**
     * Updates the unique identifier of the promotion.
     *
     * @param id the new promotion id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the display name of the promotion.
     *
     * @return the promotion name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the display name of the promotion.
     *
     * @param name the new promotion name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the date the promotion becomes active.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Updates the date the promotion becomes active.
     *
     * @param startDate the new start date
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the date the promotion stops being active.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Updates the date the promotion stops being active.
     *
     * @param endDate the new end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Checks whether this promotion is active on the given date.
     *
     * @param date the date to check
     * @return true if the date is within the promotion's validity range
     */

    public boolean isActive(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Calculates the discount amount, in currency, that this promotion
     * would grant to the given sale. Each subclass implements its own
     * calculation strategy.
     *
     * @param sale the sale to evaluate
     * @return the discount amount in currency
     */
    public abstract double calculateDiscount(Sale sale);
}