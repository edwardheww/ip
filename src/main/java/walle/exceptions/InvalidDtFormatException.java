package walle.exceptions;

/**
 * Thrown when a user-supplied datetime string doesn't match the expected
 * {@code yyyy-MM-dd HHmm} format.
 */
public class InvalidDtFormatException extends WALLEException {

    /**
     * Creates an InvalidDtFormatException.
     */
    public InvalidDtFormatException() {
        super("\n   ERROR :( Please use the datetime format yyyy-MM-dd HHmm\n");
    }

}
