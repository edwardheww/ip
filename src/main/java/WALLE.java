import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WALLE {

    private static ArrayList<Task> memory; // Memory of what user says
    private static Ui ui; // Class handling user interactions
    private static Storage storage; // Class handling loading/saving of memory file

    // Main program, running of WALLE chatbot
    public static void main(String[] args) {
        // Initialise WALLE's fields
        WALLE.memory = new ArrayList<>();
        WALLE.ui = new Ui();
        WALLE.storage = new Storage("src/main/data/memory.txt");

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
                // List tasks
                if (input.equals("list")) {
                    WALLE.list();
                }

                // Mark a task
                else if (input.matches("mark \\d")) {
                    int pos = Integer.valueOf(input.split(" ")[1]);
                    WALLE.mark(pos);
                    updateMemoryFile();
                }

                // Unmark a task
                else if (input.matches("unmark \\d")) {
                    int pos = Integer.valueOf(input.split(" ")[1]);
                    WALLE.unmark(pos);
                    updateMemoryFile();
                }

                // Delete a task
                else if (input.matches("delete \\d")) {
                    int pos = Integer.valueOf(input.split(" ")[1]);
                    Task tmp = memory.get(pos - 1);
                    memory.remove(pos - 1);
                    updateMemoryFile();
                    WALLE.ui.printTaskDeletionUpdate(tmp, memory.size());
                }

                // Error handling: proper task type, missing description
                else if (input.strip().equals("todo") || input.strip().equals("deadline")
                        || input.strip().equals("event")) {
                    throw new MissingDescException(input.strip());
                }

                // Add a task (in general)
                else {

                    // Current task in question
                    Task newTask;

                    // Add a ToDo Task
                    if (input.startsWith("todo")) {
                        newTask = new ToDo(input.substring(5));
                    }

                    // Add a Deadline Task
                    else if (input.startsWith("deadline")) {
                        String taskName = input.substring(9).split(" /by ")[0];
                        String endDT = input.substring(9).split(" /by ")[1];
                        try {
                            checkDtValidity(endDT);
                        } catch (InvalidDtFormatException e) {
                            WALLE.ui.printErrorMsg(e);
                            input = WALLE.ui.readCommand();
                            continue;
                        } catch (DateTimeException e) {
                            WALLE.ui.printErrorMsg(e);
                            input = WALLE.ui.readCommand();
                            continue;
                        }
                        newTask = new Deadline(taskName, convertUserDt(endDT));
                    }

                    // Add an Event Task
                    else if (input.startsWith("event")) {
                        String taskName = input.substring(6).split(" /from ")[0];
                        String startDT = input.substring(6).split(" /from ")[1].split(" /to ")[0];
                        String endDT = input.substring(6).split(" /from ")[1].split(" /to ")[1];
                        try {
                            checkDtValidity(endDT);
                            checkDtValidity(startDT);
                        } catch (InvalidDtFormatException e) {
                            WALLE.ui.printErrorMsg(e);
                            input = WALLE.ui.readCommand();
                            continue;
                        }
                        newTask = new Event(taskName, convertUserDt(startDT), convertUserDt(endDT));
                    }

                    // Add a normal Task
                    else {
                        String taskName = input.split(" ")[0];
                        throw new InvalidTaskTypeException(taskName);
                    }

                    WALLE.addToMemory(newTask);
                    updateMemoryFile();
                    WALLE.ui.printTaskAdditionUpdate(newTask, memory.size());
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

    // Check if user DT matches specified format: yyyy-MM-dd HHmm
    private static void checkDtValidity(String userDt) throws InvalidDtFormatException, DateTimeException {
        if (!userDt.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
            throw new InvalidDtFormatException();
        } else {
            int year = Integer.valueOf(userDt.split("-")[0]);
            if (year < 1)
                throw new DateTimeException("ERROR :( Year cannot be 0");
            int month = Integer.valueOf(userDt.split("-")[1]);
            if (month < 1 || month > 12)
                throw new DateTimeException("ERROR :( Month must be within 1-12");
            int day = Integer.valueOf(userDt.split(" ")[0].split("-")[2]);
            if (day < 1 || day > Month.of(month).length(year % 4 == 0))
                throw new DateTimeException("ERROR :( Month in question doesn't have this day");
            int hour = Integer.valueOf(userDt.split(" ")[1]) / 100;
            if (hour > 23)
                throw new DateTimeException("ERROR :( This hour doesn't exist on any clock???");
            int minute = Integer.valueOf(userDt.split(" ")[1]) % 100;
            if (minute > 59)
                throw new DateTimeException("ERROR :( This minute doesn't exist on any clock???");
        }
    }

    // Convert user's datetime input format to LocalDateTime
    private static LocalDateTime convertUserDt(String userDt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return LocalDateTime.parse(userDt, formatter);
    }

}
