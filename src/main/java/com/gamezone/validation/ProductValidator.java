package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Product;

/**
 * Validates the data and business rules related to products
 * (video games, consoles and accessories).
 *
 * <p>Format and range problems raise {@link InvalidDataException};
 * missing entities raise {@link ResourceNotFoundException}; and
 * rule violations raise {@link BusinessRuleException}.</p>
 */
public final class ProductValidator {

    private static final int MAX_TEXT_LENGTH = 100;

    private ProductValidator() {
    }

    /**
     * Validates that a text field is neither null nor blank and does not
     * exceed the maximum length.
     *
     * @param value the text to validate
     * @param fieldName the field name used in the error message
     * @throws InvalidDataException if the text is empty or too long
     */
    public static void validateText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(
                    "El campo '" + fieldName + "' no puede estar vacío.");
        }
        if (value.trim().length() > MAX_TEXT_LENGTH) {
            throw new InvalidDataException(
                    "El campo '" + fieldName + "' no puede superar "
                    + MAX_TEXT_LENGTH + " caracteres.");
        }
    }

    /**
     * Validates a product identifier.
     *
     * @param id the product id
     * @throws InvalidDataException if the id is empty
     */
    public static void validateId(String id) {
        validateText(id, "identificador");
    }

    /**
     * Validates a product title.
     *
     * @param title the product title
     * @throws InvalidDataException if the title is empty or too long
     */
    public static void validateTitle(String title) {
        validateText(title, "título");
    }

    /**
     * Validates a product price.
     *
     * @param price the price, which must be a finite number greater than zero
     * @throws InvalidDataException if the price is not positive
     */
    public static void validatePrice(double price) {
        if (Double.isNaN(price) || Double.isInfinite(price) || price <= 0) {
            throw new InvalidDataException(
                    "El precio debe ser un número mayor que cero.");
        }
    }

    /**
     * Validates a stock level.
     *
     * @param stock the stock, which cannot be negative
     * @throws InvalidDataException if the stock is negative
     */
    public static void validateStock(int stock) {
        if (stock < 0) {
            throw new InvalidDataException("El stock no puede ser negativo.");
        }
    }

    /**
     * Validates a requested quantity of units.
     *
     * @param quantity the number of units, which must be at least one
     * @throws InvalidDataException if the quantity is zero or negative
     */
    public static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new InvalidDataException(
                    "La cantidad debe ser mayor que cero.");
        }
    }

    /**
     * Validates every field needed to register a video game.
     *
     * @param id the product id
     * @param title the game title
     * @param price the unit price
     * @param stock the initial stock
     * @param platform the platform the game runs on
     * @param genre the game genre
     * @param ageRating the age rating (for example "E", "T", "M")
     * @throws InvalidDataException if any field is invalid
     */
    public static void validateVideoGame(String id, String title, double price,
            int stock, String platform, String genre, String ageRating) {
        validateId(id);
        validateTitle(title);
        validatePrice(price);
        validateStock(stock);
        validateText(platform, "plataforma");
        validateText(genre, "género");
        validateText(ageRating, "clasificación de edad");
    }

    /**
     * Validates every field needed to register a console.
     *
     * @param id the product id
     * @param title the console title
     * @param brand the manufacturer
     * @param model the console model
     * @param generation the console generation
     * @param price the unit price
     * @param stock the initial stock
     * @throws InvalidDataException if any field is invalid
     */
    public static void validateConsole(String id, String title, String brand,
            String model, String generation, double price, int stock) {
        validateId(id);
        validateTitle(title);
        validateText(brand, "marca");
        validateText(model, "modelo");
        validateText(generation, "generación");
        validatePrice(price);
        validateStock(stock);
    }

    /**
     * Validates that a product id is not already registered.
     *
     * @param existing the product found with that id, or {@code null}
     * @param id the id being registered
     * @throws BusinessRuleException if a product with that id already exists
     */
    public static void validateIdAvailable(Product existing, String id) {
        if (existing != null) {
            throw new BusinessRuleException(
                    "Ya existe un producto con el identificador: " + id);
        }
    }

    /**
     * Validates that a product was found.
     *
     * @param product the product returned by a search, or {@code null}
     * @param id the id that was searched
     * @throws ResourceNotFoundException if the product is {@code null}
     */
    public static void validateExists(Product product, String id) {
        if (product == null) {
            throw new ResourceNotFoundException(
                    "No se encontró el producto con identificador: " + id);
        }
    }

    /**
     * Validates that a product has enough units available.
     *
     * @param product the product being requested
     * @param quantity the requested number of units
     * @throws ResourceNotFoundException if the product is {@code null}
     * @throws InvalidDataException if the quantity is not positive
     * @throws BusinessRuleException if the stock is insufficient
     */
    public static void validateSufficientStock(Product product, int quantity) {
        if (product == null) {
            throw new ResourceNotFoundException(
                    "No se encontró el producto solicitado.");
        }
        validateQuantity(quantity);
        if (product.getStock() < quantity) {
            throw new BusinessRuleException(
                    "Stock insuficiente para '" + product.getTitle()
                    + "': disponible " + product.getStock()
                    + ", solicitado " + quantity + ".");
        }
    }
}
