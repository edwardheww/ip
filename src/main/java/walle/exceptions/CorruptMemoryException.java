package walle.exceptions;

/**
 * Thrown when a line in the memory file cannot be parsed into a task.
 */
public class CorruptMemoryException extends WALLEException {

    /**
     * Creates a CorruptMemoryException.
     */
    public CorruptMemoryException() {
        super("\n   ERROR :( Memory is corrupt...I don't remember anything...\n");
    }

}
