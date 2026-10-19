package com.gamezone.exception;

/**
 * Thrown when an operation is well formed but violates a business rule,
 * for example selling more units than the available stock or registering
 * a customer whose id already exists.
 */
public class BusinessRuleException extends GameZoneException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message the detail message, shown to the user by the UI
     */
    public BusinessRuleException(String message) {
        super(message);
    }

    /**
     * Creates the exception with a descriptive message and the original cause.
     *
     * @param message the detail message, shown to the user by the UI
     * @param cause the underlying exception that triggered this one
     */
    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
