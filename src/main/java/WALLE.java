import java.io.IOException;
import java.util.ArrayList;

public class WALLE {

    private static ArrayList<Task> memory; // Memory of what user says
    private static Ui ui; // Class handling user interactions
    private static Storage storage; // Class handling loading/saving of memory file
    private static Parser parser; // Class handling interpretation of user commands

    // Main program, running of WALLE chatbot
    public static void main(String[] args) {
        // Initialise WALLE's fields
        WALLE.memory = new ArrayList<>();
        WALLE.ui = new Ui();
        WALLE.storage = new Storage("src/main/data/memory.txt");
        WALLE.parser = new Parser();

        // Greeting when started
        WALLE.ui.greetUser();

        // Pull saved memory from previous session
        try {
            WALLE.memory = WALLE.storage.load();
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
                    Task tmp = memory.get(pos - 1);
                    memory.remove(pos - 1);
                    updateMemoryFile();
                    WALLE.ui.printTaskDeletionUpdate(tmp, memory.size());
                    break;
                }

                case TODO:
                case DEADLINE:
                case EVENT: {
                    Task newTask = WALLE.parser.parseTask(type, input);
                    WALLE.addToMemory(newTask);
                    updateMemoryFile();
                    WALLE.ui.printTaskAdditionUpdate(newTask, memory.size());
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

    // Add input to current empty slot in memory that memPos is pointing at
    private static void addToMemory(Task newTask) {
        memory.add(newTask);
    }

    // Print out user message history stored in memory
    private static void list() {
        WALLE.ui.listTasks(WALLE.memory);
    }

    // Mark specific task in memory as done
    private static void mark(int pos) {
        WALLE.memory.get(pos - 1).check();
        WALLE.ui.printTaskMarkedUpdate(WALLE.memory.get(pos - 1));
    }

    // Mark specific task in memory as not done
    private static void unmark(int pos) {
        WALLE.memory.get(pos - 1).uncheck();
        WALLE.ui.printTaskUnmarkedUpdate(WALLE.memory.get(pos - 1));
    }

    // Persist any changes to the task list made during the session
    private static void updateMemoryFile() {
        try {
            WALLE.storage.save(memory);
        } catch (IOException e) {
            WALLE.ui.printErrorMsg(e);
        }
    }

}
