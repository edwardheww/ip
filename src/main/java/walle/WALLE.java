package walle;

import java.io.IOException;

import walle.exception.WALLEException;
import walle.parser.CommandType;
import walle.parser.Parser;
import walle.storage.Storage;
import walle.task.Task;
import walle.task.TaskList;
import walle.ui.Ui;

public class WALLE {

    private static TaskList tasks; // Tasks WALLE is tracking
    private static Ui ui; // Class handling user interactions
    private static Storage storage; // Class handling loading/saving of memory file
    private static Parser parser; // Class handling interpretation of user commands

    // Main program, running of WALLE chatbot
    public static void main(String[] args) {
        // Initialise WALLE's fields
        WALLE.tasks = new TaskList();
        WALLE.ui = new Ui();
        WALLE.storage = new Storage("src/main/data/memory.txt");
        WALLE.parser = new Parser();

        // Greeting when started
        WALLE.ui.greetUser();

        // Pull saved memory from previous session
        try {
            WALLE.tasks = new TaskList(WALLE.storage.load());
        } catch (WALLEException e) {
            WALLE.ui.printErrorMsg(e);
        } catch (Exception e) {
            WALLE.ui.printErrorMsg(e);
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
                    WALLE.ui.printTaskDeletionUpdate(tmp, WALLE.tasks.size());
                    break;
                }

                case TODO:
                case DEADLINE:
                case EVENT: {
                    Task newTask = WALLE.parser.parseTask(type, input);
                    WALLE.tasks.add(newTask);
                    updateMemoryFile();
                    WALLE.ui.printTaskAdditionUpdate(newTask, WALLE.tasks.size());
                    break;
                }
                }
            } catch (WALLEException e) {
                WALLE.ui.printErrorMsg(e);
            }

            // Get next user input
            input = WALLE.ui.readCommand();
        }

        // Print goodbye message
        WALLE.ui.bidFarewell();

        // Close the Ui's input scanner
        WALLE.ui.closeScanner();
    }

    // Print out user message history stored in the task list
    private static void list() {
        WALLE.ui.listTasks(WALLE.tasks.getTasks());
    }

    // Mark specific task in the task list as done
    private static void mark(int pos) {
        WALLE.ui.printTaskMarkedUpdate(WALLE.tasks.mark(pos));
    }

    // Mark specific task in the task list as not done
    private static void unmark(int pos) {
        WALLE.ui.printTaskUnmarkedUpdate(WALLE.tasks.unmark(pos));
    }

    // Persist any changes to the task list made during the session
    private static void updateMemoryFile() {
        try {
            WALLE.storage.save(WALLE.tasks.getTasks());
        } catch (IOException e) {
            WALLE.ui.printErrorMsg(e);
        }
    }

}
