package com.gamezone.exception;

/**
 * Thrown when reading from or writing to the data files fails. It wraps the
 * original {@link java.io.IOException} as its cause and its message should
 * state which file or operation failed.
 */
public class PersistenceException extends GameZoneException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception with a descriptive message.
     *
     * @param message the detail message, shown to the user by the UI
     */
    public PersistenceException(String message) {
        super(message);
    }

    /**
     * Creates the exception with a descriptive message and the original cause.
     *
     * @param message the detail message, shown to the user by the UI
     * @param cause the underlying exception that triggered this one
     */
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
