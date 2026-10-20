package com.gamezone.exceptions;

/**
 * Thrown when the data supplied by the user is malformed or out of range,
 * for example an empty name, a negative price or an invalid e-mail.
 */
public class InvalidDataException extends GameZoneException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message the detail message, shown to the user by the UI
     */
    public InvalidDataException(String message) {
        super(message);
    }

    /**
     * Creates the exception with a descriptive message and the original cause.
     *
     * @param message the detail message, shown to the user by the UI
     * @param cause the underlying exception that triggered this one
     */
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
