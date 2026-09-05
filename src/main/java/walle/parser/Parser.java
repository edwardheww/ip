package walle.parser;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import walle.exceptions.InvalidDtFormatException;
import walle.exceptions.InvalidTaskTypeException;
import walle.exceptions.MissingArgException;
import walle.exceptions.MissingDescException;
import walle.exceptions.WALLEException;
import walle.task.Deadline;
import walle.task.Event;
import walle.task.Task;
import walle.task.ToDo;

/**
 * Makes sense of raw user input: classifies which command it is, and
 * extracts whatever data (an index, or a new {@link Task}) that command
 * needs.
 */
public class Parser {

    /**
     * Classifies the given raw input into a {@link CommandType}.
     *
     * @param input the raw command string entered by the user.
     * @return the type of command the input represents.
     * @throws InvalidTaskTypeException if the input does not match any known command.
     */
    public CommandType parseCommandType(String input) throws InvalidTaskTypeException {
        if (input.equals("list")) {
            return CommandType.LIST;
        } else if (input.matches("mark \\d+")) {
            return CommandType.MARK;
        } else if (input.matches("unmark \\d+")) {
            return CommandType.UNMARK;
        } else if (input.matches("delete \\d+")) {
            return CommandType.DELETE;
        } else if (input.strip().equals("todo") || input.startsWith("todo")) {
            return CommandType.TODO;
        } else if (input.strip().equals("deadline") || input.startsWith("deadline")) {
            return CommandType.DEADLINE;
        } else if (input.strip().equals("event") || input.startsWith("event")) {
            return CommandType.EVENT;
        } else if (input.strip().equals("find") || input.startsWith("find ")) {
            return CommandType.FIND;
        } else {
            throw new InvalidTaskTypeException(input.split(" ")[0]);
        }
    }

    /**
     * Extracts the 1-based task index from a {@code mark}/{@code unmark}/{@code delete} command.
     *
     * @param input the raw command string, e.g. {@code "mark 2"}.
     * @return the 1-based index the user referred to.
     */
    public int parseIndex(String input) {
        return Integer.valueOf(input.split(" ")[1]);
    }

    /**
     * Extracts the search keyword from a {@code find} command.
     *
     * @param input the raw command string, e.g. {@code "find book"}.
     * @return the keyword to search task descriptions for.
     * @throws MissingDescException if no keyword is given.
     */
    public String parseKeyword(String input) throws MissingDescException {
        if (input.strip().equals("find")) {
            throw new MissingDescException("find");
        }
        return input.substring(5);
    }

    /**
     * Builds the {@link Task} described by a {@code todo}/{@code deadline}/{@code event} command.
     *
     * @param type  the command type, as classified by {@link #parseCommandType(String)}.
     * @param input the raw command string.
     * @return the new task to add.
     * @throws WALLEException if the description is missing, or a datetime is malformed.
     */
    public Task parseTask(CommandType type, String input) throws WALLEException {
        switch (type) {
            case TODO:
                if (input.strip().equals("todo")) {
                    throw new MissingDescException("todo");
                }
                return new ToDo(input.substring(5));

            case DEADLINE:
                if (input.strip().equals("deadline")) {
                    throw new MissingDescException("deadline");
                }
                String[] deadlineParts = input.substring(9).split(" /by ");
                if (deadlineParts.length < 2) {
                    throw new MissingArgException("deadline", "/by");
                }
                return new Deadline(deadlineParts[0], parseDateTime(deadlineParts[1]));

            case EVENT:
                if (input.strip().equals("event")) {
                    throw new MissingDescException("event");
                }
                String[] eventParts = input.substring(6).split(" /from ");
                if (eventParts.length < 2) {
                    throw new MissingArgException("event", "/from");
                }
                String[] eventTimes = eventParts[1].split(" /to ");
                if (eventTimes.length < 2) {
                    throw new MissingArgException("event", "/to");
                }
                return new Event(eventParts[0], parseDateTime(eventTimes[0]), parseDateTime(eventTimes[1]));

            default:
                throw new InvalidTaskTypeException(input.split(" ")[0]);
        }
    }

    // Parse user's datetime input (format: yyyy-MM-dd HHmm) into a LocalDateTime
    private LocalDateTime parseDateTime(String userDt) throws InvalidDtFormatException {
        try {
            return LocalDateTime.parse(userDt, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (DateTimeException e) {
            throw new InvalidDtFormatException();
        }
    }

}
