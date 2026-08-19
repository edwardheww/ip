import java.util.Scanner;

public class WALLE {

    // Memory of what user says
    private static String[] memory;
    private static int memPos;

    public static void main(String[] args) {

        // Greeting when started
        String greeting = "\n"
                + "Hey! I'm WALLE.\n"
                + "What can I do for you?\n"
                + "———————————————————————\n";
        System.out.println(greeting);

        // Initialise WALLE's memory & position pointer
        WALLE.memory = new String[100];
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
            if (input.equals("list")) {
                WALLE.list();
            } else {
                WALLE.addToMemory(input);
                System.out.printf("\n   added: %s\n\n", input);
            }
            input = scanner.nextLine();
        }

        // Print goodbye message
        System.out.println("\n  Bye bye! See you next time, I hope!\n");

        // Close scanner
        scanner.close();

    }

    private static void addToMemory(String input) {

        // Add input to current empty slot in memory that memPos is pointing at
        memory[memPos] = input;

        // Increment memPos to point at next empty slot
        memPos++;

    }

    private static void list() {

        // Print out user message history stored in memory
        System.out.println(""); // Used for newline
        for (int pos = 0; pos < memPos; pos++) {
            System.out.println(
                    "   "
                            + (pos + 1)
                            + ". "
                            + memory[pos]);
        }
        System.out.println(""); // Used for newline

    }

}
