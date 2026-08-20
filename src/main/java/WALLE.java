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
            if (input.equals("list")) { // list tasks
                WALLE.list();
            } else if (input.matches("mark \\d")) { // mark a task
                int pos = Integer.valueOf(input.split(" ")[1]);
                WALLE.mark(pos);
            } else if (input.matches("unmark \\d")) { // unmark a task
                int pos = Integer.valueOf(input.split(" ")[1]);
                WALLE.unmark(pos);
            } else {
                WALLE.addToMemory(new Task(input));
                System.out.printf("\n   added: %s\n\n", input);
            }
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
