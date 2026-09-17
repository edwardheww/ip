package walle;

/**
 * WALLE's reply to one line of user input.
 *
 * @param text    the message to show the user.
 * @param isError whether the reply represents an error (e.g. a bad command
 *                or a corrupted save file), so a caller such as the GUI can
 *                style it differently from a normal reply.
 */
public record Response(String text, boolean isError) {
}
