import java.util.Scanner;

public class WALLE {

    // Memory of what user says
    private static Task[] memory;
    private static int memPos;

    public static void main(String[] args) {

        // Greeting when started
        String greeting = "\n"
                + "Hey! I'm WALLE.\n"
                + "What can I do for you?\n"
                + "———————————————————————\n";
        System.out.println(greeting);

        // Initialise WALLE's memory & position pointer
        WALLE.memory = new Task[100];
        WALLE.memPos = 0;

        // Start process of echoing user input
        WALLE.interactUntilBye();

    }

    private static void interactUntilBye() {

        // Listen for first user input
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // While input is not 'bye', echo input back to user
        while (!input.equals("bye")) {

            // List tasks
            if (input.equals("list")) {
                WALLE.list();
            }

            // Mark a task
            else if (input.matches("mark \\d")) {
                int pos = Integer.valueOf(input.split(" ")[1]);
                WALLE.mark(pos);
            }

            // Unmark a task
            else if (input.matches("unmark \\d")) {
                int pos = Integer.valueOf(input.split(" ")[1]);
                WALLE.unmark(pos);
            }

            // Add a task (in general)
            else {

                // Current task in question
                Task newTask;

                // Add a ToDo Task
                if (input.startsWith("todo ")) {
                    newTask = new ToDo(input.substring(5));
                }

                // Add a Deadline Task
                else if (input.startsWith("deadline ")) {
                    String taskName = input.substring(9).split(" /by ")[0];
                    String endDT = input.substring(9).split(" /by ")[1];
                    newTask = new Deadline(taskName, endDT);
                }

                // Add an Event Task
                else if (input.startsWith("event ")) {
                    String taskName = input.substring(6).split(" /from ")[0];
                    String startDT = input.substring(6).split(" /from ")[1].split(" /to ")[0];
                    String endDT = input.substring(6).split(" /from ")[1].split(" /to ")[1];
                    newTask = new Event(taskName, startDT, endDT);
                }

                // Add a normal Task
                else {
                    newTask = new Task(input);
                }

                WALLE.addToMemory(newTask);
                System.out.printf("\n   added: %s\n\n", input);
            }

            // Get next user input
            input = scanner.nextLine();
        }

        // Print goodbye message
        System.out.println("\n  Bye bye! See you next time, I hope!\n");

        // Close scanner
        scanner.close();

    }

    private static void addToMemory(Task newTask) {

        // Add input to current empty slot in memory that memPos is pointing at
        memory[memPos] = newTask;

        // Increment memPos to point at next empty slot
        memPos++;

    }

    private static void list() {

        // Print out user message history stored in memory
        System.out.println("\n  Here are the tasks in your list:"); // Used for newline
        for (int pos = 0; pos < memPos; pos++) {
            System.out.println("   "
                    + (pos + 1)
                    + "."
                    + memory[pos]);
        }
        System.out.println(""); // Used for newline

    }

    private static void mark(int pos) {

        // Mark specific task in memory as done
        WALLE.memory[pos - 1].check();
        System.out.printf("\n    Nice! I've marked this task as done:\n"
                + "        %s\n\n", WALLE.memory[pos - 1]);

    }

    private static void unmark(int pos) {

        // Mark specific task in memory as not done
        WALLE.memory[pos - 1].uncheck();
        System.out.printf("\n    OK, I've marked this task as not done yet:\n"
                + "        %s\n\n", WALLE.memory[pos - 1]);

    }

}
