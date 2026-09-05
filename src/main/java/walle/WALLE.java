package walle;

import java.io.IOException;

import walle.exceptions.WALLEException;
import walle.parser.CommandType;
import walle.parser.Parser;
import walle.storage.Storage;
import walle.task.Task;
import walle.task.TaskList;
import walle.ui.Ui;

/**
 * Entry point for the WALLE chatbot. Wires together the {@link Ui}, {@link Storage},
 * {@link Parser}, and {@link TaskList} components, loads any previously saved tasks,
 * and drives the main read-command loop until the user says {@code bye}.
 */
// CHECKSTYLE.OFF: AbbreviationAsWordInName - "WALLE" is the chatbot's actual name, not an abbreviation to shorten.
public class WALLE {
    // CHECKSTYLE.ON: AbbreviationAsWordInName

    private static TaskList tasks; // Tasks WALLE is tracking
    private static Ui ui; // Class handling user interactions
    private static Storage storage; // Class handling loading/saving of memory file
    private static Parser parser; // Class handling interpretation of user commands

    /**
     * Starts WALLE: greets the user, loads saved tasks, then processes user
     * input until the user exits.
     *
     * @param args unused command-line arguments.
     */
    public static void main(String[] args) {
        // Initialise WALLE's fields
        WALLE.tasks = new TaskList();
        WALLE.ui = new Ui();
        WALLE.storage = new Storage("src/main/data/memory.txt");
        WALLE.parser = new Parser();

        // Greeting when started
        System.out.println(WALLE.ui.formatGreeting());

        // Pull saved memory from previous session
        try {
            WALLE.tasks = new TaskList(WALLE.storage.load());
        } catch (WALLEException e) {
            System.out.println(WALLE.ui.formatErrorMsg(e));
        } catch (Exception e) {
            System.out.println(WALLE.ui.formatErrorMsg(e));
        }

        // Start process of echoing user input
        WALLE.interactUntilBye();
    }

    // Allow WALLE to continue running until user quits by saying 'bye'
    private static void interactUntilBye() {
        // Listen for first user input
        String input = WALLE.ui.readCommand();

        // While input is not 'bye', echo input back to user
        while (!input.equals("bye")) {
            try {
                CommandType type = WALLE.parser.parseCommandType(input);

                switch (type) {
                    case LIST:
                        WALLE.list();
                        break;

                    case MARK:
                        WALLE.mark(WALLE.parser.parseIndex(input));
                        updateMemoryFile();
                        break;

                    case UNMARK:
                        WALLE.unmark(WALLE.parser.parseIndex(input));
                        updateMemoryFile();
                        break;

                    case DELETE: {
                        int pos = WALLE.parser.parseIndex(input);
                        Task tmp = WALLE.tasks.delete(pos);
                        updateMemoryFile();
                        System.out.println(WALLE.ui.formatTaskDeletionUpdate(tmp, WALLE.tasks.size()));
                        break;
                    }

                    case TODO:
                    case DEADLINE:
                    case EVENT: {
                        Task newTask = WALLE.parser.parseTask(type, input);
                        WALLE.tasks.add(newTask);
                        updateMemoryFile();
                        System.out.println(WALLE.ui.formatTaskAdditionUpdate(newTask, WALLE.tasks.size()));
                        break;
                    }

                    case FIND: {
                        String keyword = WALLE.parser.parseKeyword(input);
                        System.out.println(WALLE.ui.formatMatchingTasks(WALLE.tasks.find(keyword)));
                        break;
                    }

                    default:
                        break;
                }
            } catch (WALLEException e) {
                System.out.println(WALLE.ui.formatErrorMsg(e));
            }

            // Get next user input
            input = WALLE.ui.readCommand();
        }

        // Print goodbye message
        System.out.println(WALLE.ui.formatFarewell());

        // Close the Ui's input scanner
        WALLE.ui.closeScanner();
    }

    // Print out user message history stored in the task list
    private static void list() {
        System.out.println(WALLE.ui.formatTaskList(WALLE.tasks.getTasks()));
    }

    // Mark specific task in the task list as done
    private static void mark(int pos) {
        System.out.println(WALLE.ui.formatTaskMarkedUpdate(WALLE.tasks.mark(pos)));
    }

    // Mark specific task in the task list as not done
    private static void unmark(int pos) {
        System.out.println(WALLE.ui.formatTaskUnmarkedUpdate(WALLE.tasks.unmark(pos)));
    }

    // Persist any changes to the task list made during the session
    private static void updateMemoryFile() {
        try {
            WALLE.storage.save(WALLE.tasks.getTasks());
        } catch (IOException e) {
            System.out.println(WALLE.ui.formatErrorMsg(e));
        }
    }

}
