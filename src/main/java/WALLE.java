import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.ArrayList;

public class WALLE {

    // Memory of what user says
    private static ArrayList<Task> memory;

    // Main program, running of WALLE chatbot
    public static void main(String[] args) {
        // Greeting when started
        String greeting = "\n"
                + "Hey! I'm WALLE.\n"
                + "What can I do for you?\n"
                + "———————————————————————\n";
        System.out.println(greeting);

        // Initialise WALLE's memory
        WALLE.memory = new ArrayList<>();

        // Pull saved memory from previous session
        try {
            WALLE.pullSavedMemory();
        } catch (IOException e) {
            System.out.println("\n    ERROR: " + e.getMessage() + " :(\n");
        } catch (WALLEException e) {
            System.out.println(e.getMessage());
        }

        // Start process of echoing user input
        WALLE.interactUntilBye();
    }

    // Allow WALLE to continue running until user quits by saying 'bye'
    private static void interactUntilBye() {
        // Listen for first user input
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

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
                    System.out.println("\n    Got it! I've removed the task:\n"
                            + "        " + tmp
                            + "\n    " + memory.size() + " task(s) left, let's go :D\n");
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
                            System.out.println(e.getMessage());
                            input = scanner.nextLine();
                            continue;
                        } catch (DateTimeException e) {
                            System.out.println(e.getMessage());
                            input = scanner.nextLine();
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
                            System.out.println(e.getMessage());
                            input = scanner.nextLine();
                            continue;
                        }
                        newTask = new Event(taskName, convertUserDt(startDT), convertUserDt(endDT));
                    }

                    // Delete a Task
                    else if (input.startsWith("delete")) {
                        int deleteWhich = Integer.valueOf(input.substring(7));
                        WALLE.delete(deleteWhich);
                        updateMemoryFile();
                        continue;
                    }

                    // Add a normal Task
                    else {
                        String taskName = input.split(" ")[0];
                        throw new InvalidTaskTypeException(taskName);
                    }

                    WALLE.addToMemory(newTask);
                    updateMemoryFile();
                    System.out.println("\n    Got it! I've added the task:");
                    System.out.printf("        %s\n", newTask);
                    System.out.printf("    Now you have %d task(s) on your list!\n\n", memory.size());
                }
            } catch (WALLEException e) {
                System.out.println(e.getMessage());
            }

            // Get next user input
            input = scanner.nextLine();
        }

        // Print goodbye message
        System.out.println("\n  Bye bye! See you next time, I hope!\n");

        // Close scanner
        scanner.close();
    }

    // Add input to current empty slot in memory that memPos is pointing at
    private static void addToMemory(Task newTask) {
        memory.add(newTask);
    }

    // Print out user message history stored in memory
    private static void list() {
        System.out.println("\n  Here are the tasks in your list:"); // Used for newline
        for (int pos = 0; pos < memory.size(); pos++) {
            System.out.println("   "
                    + (pos + 1)
                    + "."
                    + memory.get(pos));
        }
        System.out.println(""); // Used for newline
    }

    // Mark specific task in memory as done
    private static void mark(int pos) {
        WALLE.memory.get(pos - 1).check();
        System.out.printf("\n    Nice! I've marked this task as done:\n"
                + "        %s\n\n", WALLE.memory.get(pos - 1));
    }

    // Mark specific task in memory as not done
    private static void unmark(int pos) {
        WALLE.memory.get(pos - 1).uncheck();
        System.out.printf("\n    OK, I've marked this task as not done yet:\n"
                + "        %s\n\n", WALLE.memory.get(pos - 1));
    }

    // Delete specific task in memory
    private static void delete(int pos) {
        Task tmp = WALLE.memory.get(pos);
        WALLE.memory.remove(pos - 1);
        System.out.printf("\n   OK, I've removed the following task:\n"
                + "        %s\n"
                + "    Now you have %d  task(s) on your list!\n\n",
                tmp, memory.size());
    }

    /**
     * Pulls saved memory from the previous session into the current session.
     *
     * <p>Expected line formats in the memory file:
     * <ul>
     *   <li>ToDo: {@code <type>;<checkmark>;<task>}</li>
     *   <li>Deadline: {@code <type>;<checkmark>;<task>;<endDT>}</li>
     *   <li>Event: {@code <type>;<checkmark>;<task>;<startDT>;<endDT>}</li>
     * </ul>
     *
     * @throws FileNotFoundException if the memory file cannot be found.
     * @throws IOException if the memory file cannot be created or read.
     * @throws WALLEException if a line in the memory file is corrupted.
     */
    private static void pullSavedMemory() throws FileNotFoundException, IOException, WALLEException {
        File memFile = new File("src/main/data/memory.txt");

        // Ensuring file exists by creating file if nonexistent
        if (!memFile.exists()) {
            File parent = memFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            memFile.createNewFile();
        }

        Scanner memScanner = new Scanner(memFile);

        while (memScanner.hasNext()) { // Handles saved tasks one by one
            try {
                String[] taskData = memScanner.nextLine().split(";");
                // Handling if saved task is a ToDo
                if (taskData[0].equals("T")) {
                    String task = taskData[2];
                    boolean checked = taskData[1].equals("X");
                    memory.add(new ToDo(task, checked));
                }

                // Handling if saved task is a Deadline
                else if (taskData[0].equals("D")) {
                    String task = taskData[2];
                    boolean checked = taskData[1].equals("X");
                    String endDT = taskData[3];
                    memory.add(new Deadline(task, LocalDateTime.parse(endDT), checked));
                }

                else if (taskData[0].equals("E")) {
                    String task = taskData[2];
                    boolean checked = taskData[1].equals("X");
                    String startDT = taskData[3];
                    String endDT = taskData[4];
                    memory.add(new Event(task, LocalDateTime.parse(startDT), LocalDateTime.parse(endDT), checked));
                }
            } catch (Exception e) {
                memScanner.close();
                throw new CorruptMemoryException();
            }
        }

        memScanner.close();
    }

    // Update memory file with any changes to task list made during session
    private static void updateMemoryFile() {
        try {
            FileWriter memFw = new FileWriter("src/main/data/memory.txt");
            for (Task task : memory) {
                String memInput = task.getMemoryFormat();
                memFw.write(memInput + System.lineSeparator());
            }
            memFw.close();
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage() + " :(");
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
