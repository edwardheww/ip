package walle.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import walle.exceptions.InvalidDtFormatException;
import walle.exceptions.InvalidTaskTypeException;
import walle.exceptions.MissingArgException;
import walle.exceptions.MissingDescException;
import walle.task.Task;

public class ParserTest {

    private final Parser parser = new Parser();

    @Test
    public void parseCommandType_listCommand_returnsListType() {
        assertEquals(CommandType.LIST, parser.parseCommandType("list"));
    }

    @Test
    public void parseCommandType_markUnmarkDeleteCommands_returnCorrespondingTypes() {
        assertEquals(CommandType.MARK, parser.parseCommandType("mark 2"));
        assertEquals(CommandType.UNMARK, parser.parseCommandType("unmark 2"));
        assertEquals(CommandType.DELETE, parser.parseCommandType("delete 2"));
    }

    @Test
    public void parseCommandType_todoDeadlineEventCommands_returnCorrespondingTypes() {
        assertEquals(CommandType.TODO, parser.parseCommandType("todo read book"));
        assertEquals(CommandType.DEADLINE, parser.parseCommandType("deadline submit report /by 2026-09-10 2359"));
        assertEquals(CommandType.EVENT, parser.parseCommandType("event meeting /from 2026-09-05 1400 /to 2026-09-05 1500"));
    }

    @Test
    public void parseCommandType_unknownCommand_exceptionThrown() {
        assertThrows(InvalidTaskTypeException.class, () -> parser.parseCommandType("foobar"));
    }

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    @Test
    public void parseTask_validDeadline_returnsCorrectlyFormattedTask() {
        Task task = parser.parseTask(CommandType.DEADLINE, "deadline submit report /by 2026-09-10 2359");
        String expectedDate = LocalDateTime.of(2026, 9, 10, 23, 59).format(DATE_FORMAT);
        assertEquals("[D][ ] submit report (by: " + expectedDate + ")", task.toString());
    }

    @Test
    public void parseTask_validEvent_returnsCorrectlyFormattedTask() {
        Task task = parser.parseTask(CommandType.EVENT, "event team meeting /from 2026-09-05 1400 /to 2026-09-05 1500");
        String expectedStart = LocalDateTime.of(2026, 9, 5, 14, 0).format(DATE_FORMAT);
        String expectedEnd = LocalDateTime.of(2026, 9, 5, 15, 0).format(DATE_FORMAT);
        assertEquals("[E][ ] team meeting (from: " + expectedStart + " to: " + expectedEnd + ")", task.toString());
    }

    @Test
    public void parseTask_missingDescription_exceptionThrown() {
        assertThrows(MissingDescException.class, () -> parser.parseTask(CommandType.TODO, "todo"));
        assertThrows(MissingDescException.class, () -> parser.parseTask(CommandType.DEADLINE, "deadline"));
        assertThrows(MissingDescException.class, () -> parser.parseTask(CommandType.EVENT, "event"));
    }

    @Test
    public void parseTask_deadlineMissingByArgument_exceptionThrown() {
        assertThrows(MissingArgException.class, () -> parser.parseTask(CommandType.DEADLINE, "deadline submit report"));
    }

    @Test
    public void parseTask_eventMissingFromArgument_exceptionThrown() {
        assertThrows(MissingArgException.class, () -> parser.parseTask(CommandType.EVENT, "event meeting"));
    }

    @Test
    public void parseTask_eventMissingToArgument_exceptionThrown() {
        assertThrows(MissingArgException.class,
                () -> parser.parseTask(CommandType.EVENT, "event meeting /from 2026-09-05 1400"));
    }

    @Test
    public void parseTask_invalidDatetimeFormat_exceptionThrown() {
        assertThrows(InvalidDtFormatException.class,
                () -> parser.parseTask(CommandType.DEADLINE, "deadline submit report /by not-a-date"));
    }

}
