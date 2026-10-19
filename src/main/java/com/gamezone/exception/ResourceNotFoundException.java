package com.gamezone.exception;

/**
 * Thrown when an entity requested by id (product, accessory, customer,
 * seller, sale, return, etc.) does not exist in the system.
 */
public class ResourceNotFoundException extends GameZoneException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message the detail message, shown to the user by the UI
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates the exception with a descriptive message and the original cause.
     *
     * @param message the detail message, shown to the user by the UI
     * @param cause the underlying exception that triggered this one
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
