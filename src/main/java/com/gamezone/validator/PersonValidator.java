package com.gamezone.validator;

import com.gamezone.exception.BusinessRuleException;
import com.gamezone.exception.InvalidDataException;
import com.gamezone.exception.ResourceNotFoundException;
import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import java.util.regex.Pattern;

/**
 * Validates the data and business rules related to people
 * (customers and sellers).
 */
public final class PersonValidator {

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[\\p{L} .'-]{2,60}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9]{7,15}$");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private PersonValidator() {
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(
                    "El campo '" + fieldName + "' no puede estar vacío.");
        }
    }

    /**
     * Validates a person identifier.
     *
     * @param id the person id
     * @throws InvalidDataException if the id is empty
     */
    public static void validateId(String id) {
        requireText(id, "identificación");
    }

    /**
     * Validates a person name: letters, spaces, dots, apostrophes and
     * hyphens, between 2 and 60 characters.
     *
     * @param name the full name
     * @throws InvalidDataException if the name is empty or has invalid characters
     */
    public static void validateName(String name) {
        requireText(name, "nombre");
        if (!NAME_PATTERN.matcher(name.trim()).matches()) {
            throw new InvalidDataException(
                    "El nombre debe tener entre 2 y 60 letras, sin números "
                    + "ni símbolos especiales.");
        }
    }

    /**
     * Validates a phone number. Spaces and hyphens are ignored; the rest must
     * be 7 to 15 digits with an optional leading "+".
     *
     * @param phone the phone number
     * @throws InvalidDataException if the phone number is not valid
     */
    public static void validatePhone(String phone) {
        requireText(phone, "teléfono");
        String cleaned = phone.replaceAll("[\\s-]", "");
        if (!PHONE_PATTERN.matcher(cleaned).matches()) {
            throw new InvalidDataException(
                    "El teléfono debe tener entre 7 y 15 dígitos.");
        }
    }

    /**
     * Validates an e-mail address.
     *
     * @param email the e-mail address
     * @throws InvalidDataException if the e-mail format is not valid
     */
    public static void validateEmail(String email) {
        requireText(email, "correo electrónico");
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new InvalidDataException(
                    "El correo electrónico no tiene un formato válido.");
        }
    }

    /**
     * Validates every field needed to register a customer.
     *
     * @param id the customer id
     * @param name the customer name
     * @param phone the customer phone number
     * @param email the customer e-mail
     * @throws InvalidDataException if any field is invalid
     */
    public static void validateCustomer(String id, String name, String phone,
            String email) {
        validateId(id);
        validateName(name);
        validatePhone(phone);
        validateEmail(email);
    }

    /**
     * Validates every field needed to register a seller.
     *
     * @param id the seller id
     * @param name the seller name
     * @param phone the seller phone number
     * @param employeeCode the employee code
     * @param shift the work shift
     * @throws InvalidDataException if any field is invalid
     */
    public static void validateSeller(String id, String name, String phone,
            String employeeCode, String shift) {
        validateId(id);
        validateName(name);
        validatePhone(phone);
        requireText(employeeCode, "código de empleado");
        requireText(shift, "turno");
    }

    /**
     * Validates that a customer id is not already registered.
     *
     * @param existing the customer found with that id, or {@code null}
     * @param id the id being registered
     * @throws BusinessRuleException if a customer with that id already exists
     */
    public static void validateCustomerIdAvailable(Customer existing, String id) {
        if (existing != null) {
            throw new BusinessRuleException(
                    "Ya existe un cliente con la identificación: " + id);
        }
    }

    /**
     * Validates that a customer was found.
     *
     * @param customer the customer returned by a search, or {@code null}
     * @param id the id that was searched
     * @throws ResourceNotFoundException if the customer is {@code null}
     */
    public static void validateCustomerExists(Customer customer, String id) {
        if (customer == null) {
            throw new ResourceNotFoundException(
                    "No se encontró el cliente con identificación: " + id);
        }
    }

    /**
     * Validates that a seller was found.
     *
     * @param seller the seller returned by a search, or {@code null}
     * @param id the id that was searched
     * @throws ResourceNotFoundException if the seller is {@code null}
     */
    public static void validateSellerExists(Seller seller, String id) {
        if (seller == null) {
            throw new ResourceNotFoundException(
                    "No se encontró el vendedor con identificación: " + id);
        }
    }
}
