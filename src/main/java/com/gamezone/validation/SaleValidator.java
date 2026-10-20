package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Seller;
import java.util.List;

/**
 * Validates the data and business rules involved in registering a sale.
 * Existence and stock checks delegate to {@link PersonValidator} and
 * {@link ProductValidator} so each rule lives in a single place.
 */
public final class SaleValidator {

    private SaleValidator() {
    }

    /**
     * Validates the list of product ids of a sale.
     *
     * @param productIds the ids of the products being sold
     * @throws InvalidDataException if the list is null, empty or contains
     *                              empty ids
     */
    public static void validateProductIds(List<String> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new InvalidDataException(
                    "Una venta debe contener al menos un producto.");
        }
        for (String id : productIds) {
            ProductValidator.validateId(id);
        }
    }

    /**
     * Validates that the customer of the sale exists.
     *
     * @param customer the customer found, or {@code null}
     * @param id the customer id that was searched
     * @throws ResourceNotFoundException if the customer is {@code null}
     */
    public static void validateCustomerExists(Customer customer, String id) {
        PersonValidator.validateCustomerExists(customer, id);
    }

    /**
     * Validates that the seller of the sale exists.
     *
     * @param seller the seller found, or {@code null}
     * @param id the seller id that was searched
     * @throws ResourceNotFoundException if the seller is {@code null}
     */
    public static void validateSellerExists(Seller seller, String id) {
        PersonValidator.validateSellerExists(seller, id);
    }

    /**
     * Validates that a product or accessory of the sale exists.
     *
     * @param product the product found, or {@code null}
     * @param id the product id that was searched
     * @throws ResourceNotFoundException if the product is {@code null}
     */
    public static void validateProductExists(Product product, String id) {
        ProductValidator.validateExists(product, id);
    }

    /**
     * Validates that there is enough stock for the requested quantity.
     *
     * @param product the product being sold
     * @param quantity the number of units requested
     * @throws BusinessRuleException if the stock is insufficient
     */
    public static void validateStock(Product product, int quantity) {
        ProductValidator.validateSufficientStock(product, quantity);
    }

    /**
     * Validates that every product receiving an extended warranty is part
     * of the sale. A {@code null} or empty list means no extended warranty.
     *
     * @param productIds the ids of the products in the sale
     * @param warrantyProductIds the ids that should receive the warranty
     * @throws BusinessRuleException if a warranty id is not in the sale
     */
    public static void validateExtendedWarranties(List<String> productIds,
            List<String> warrantyProductIds) {
        if (warrantyProductIds == null) {
            return;
        }
        for (String id : warrantyProductIds) {
            if (productIds == null || !productIds.contains(id)) {
                throw new BusinessRuleException(
                        "No se puede asignar garantía extendida al producto '"
                        + id + "' porque no hace parte de la venta.");
            }
        }
    }
}
