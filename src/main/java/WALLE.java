import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class WALLE {

    // Memory of what user says
    private static ArrayList<Task> memory;

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
                        newTask = new Deadline(taskName, endDT);
                    }

                    // Add an Event Task
                    else if (input.startsWith("event")) {
                        String taskName = input.substring(6).split(" /from ")[0];
                        String startDT = input.substring(6).split(" /from ")[1].split(" /to ")[0];
                        String endDT = input.substring(6).split(" /from ")[1].split(" /to ")[1];
                        newTask = new Event(taskName, startDT, endDT);
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

    /*
     * Pull saved memory from past session into current context
     * Format for ToDo: <type>:<checkmark>:<task>
     * Format for Deadline: <type>:<checkmark>:<task>:<endDT>
     * Format for Event: <type>:<checkmark>:<task>:<startDT>:<endDT>
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
                String[] taskData = memScanner.nextLine().split(":");
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
                    memory.add(new Deadline(task, endDT, checked));
                }

                else if (taskData[0].equals("E")) {
                    String task = taskData[2];
                    boolean checked = taskData[1].equals("X");
                    String startDT = taskData[3];
                    String endDT = taskData[4];
                    memory.add(new Event(task, startDT, endDT, checked));
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

}
