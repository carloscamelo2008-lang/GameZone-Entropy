package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.WarrantyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Contains the business rules related to product warranties, including
 * assignment of basic and extended warranties, and queries about their
 * validity.
 */
public class WarrantyService {

    private final WarrantyRepository warrantyRepository;
    private final List<Warranty> warranties;

    /**
     * Creates a warranty service and loads previously stored warranties.
     *
     * @param warrantyRepository repository used to persist warranties
     */
    public WarrantyService(WarrantyRepository warrantyRepository) {
        this.warrantyRepository = warrantyRepository;
        this.warranties = warrantyRepository.loadAll();
    }

    /**
     * Creates and persists an automatic basic warranty for the given
     * product, associated to the given sale.
     *
     * @param product   the product covered by the warranty
     * @param sale      the sale in which the product was purchased
     * @param startDate the date the warranty coverage begins
     * @return the newly created basic warranty
     */
    public BasicWarranty assignBasicWarranty(Product product, Sale sale, LocalDate startDate) {
        BasicWarranty warranty = new BasicWarranty(generateId(), product, sale, startDate);
        warranties.add(warranty);
        warrantyRepository.saveAll(warranties);
        return warranty;
    }

    /**
     * Creates and persists an extended warranty for the given product,
     * associated to the given sale.
     *
     * @param product   the product covered by the warranty
     * @param sale      the sale in which the product was purchased
     * @param startDate the date the warranty coverage begins
     * @return the newly created extended warranty
     */
    public ExtendedWarranty assignExtendedWarranty(Product product, Sale sale, LocalDate startDate) {
        ExtendedWarranty warranty = new ExtendedWarranty(generateId(), product, sale, startDate);
        warranties.add(warranty);
        warrantyRepository.saveAll(warranties);
        return warranty;
    }

    /**
     * Finds the warranty associated to a specific product within a
     * specific sale.
     *
     * @param productId the id of the covered product
     * @param saleId    the composite identifier of the sale, built with
     *                  {@code sale.getDate() + "|" + sale.getCustomer().getId()}
     * @return the matching warranty, or null if not found
     */
    public Warranty findWarrantyByProduct(String productId, String saleId) {
        for (Warranty warranty : warranties) {
            String warrantySaleId = warranty.getSale().getDate() + "|" + warranty.getSale().getCustomer().getId();
            if (warranty.getProduct().getId().equals(productId) && warrantySaleId.equals(saleId)) {
                return warranty;
            }
        }
        return null;
    }

    /**
     * Returns all registered warranties.
     *
     * @return the complete list of warranties
     */
    public List<Warranty> listAllWarranties() {
        return warranties;
    }

    /**
     * Returns all warranties currently active on today's date.
     *
     * @return the list of active warranties
     */
    public List<Warranty> listActiveWarranties() {
        List<Warranty> activeWarranties = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Warranty warranty : warranties) {
            if (warranty.isActive(today)) {
                activeWarranties.add(warranty);
            }
        }

        return activeWarranties;
    }

    /**
     * Returns all warranties whose coverage ends within the given
     * number of days from today, and that have not expired yet.
     *
     * @param daysAhead how many days ahead to look for expirations
     * @return the list of warranties expiring soon
     */
    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {
        List<Warranty> expiringSoon = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(daysAhead);

        for (Warranty warranty : warranties) {
            LocalDate endDate = warranty.getEndDate();
            if (!endDate.isBefore(today) && !endDate.isAfter(limit)) {
                expiringSoon.add(warranty);
            }
        }

        return expiringSoon;
    }

    /**
     * Generates a unique identifier for a new warranty.
     *
     * @return a newly generated warranty id
     */
    private String generateId() {
        return "WAR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
