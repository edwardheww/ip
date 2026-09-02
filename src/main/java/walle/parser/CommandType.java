package walle.parser;

/**
 * The kinds of command WALLE recognises, as classified by {@link Parser}.
 */
public enum CommandType {
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    FIND
}
