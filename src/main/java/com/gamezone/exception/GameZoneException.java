package com.gamezone.exception;

/**
 * Base class of every application-specific exception of GameZone.
 * It is unchecked so that services and repositories can throw it without
 * changing their method signatures, and so that the user interface can
 * catch the whole family with a single type when needed.
 *
 * <p>Subclasses: {@link ResourceNotFoundException},
 * {@link BusinessRuleException}, {@link InvalidDataException} and
 * {@link PersistenceException}.</p>
 */
public class GameZoneException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message the detail message, shown to the user by the UI
     */
    public GameZoneException(String message) {
        super(message);
    }

    /**
     * Creates the exception with a descriptive message and the original cause.
     *
     * @param message the detail message, shown to the user by the UI
     * @param cause the underlying exception that triggered this one
     */
    public GameZoneException(String message, Throwable cause) {
        super(message, cause);
    }
}
