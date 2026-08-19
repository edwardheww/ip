import java.util.Scanner;

public class WALLE {

    // Memory of what user says
    private static String[] memory;

    public static void main(String[] args) {

        // Greeting when started
        String greeting = "\n"
                + "Hey! I'm WALLE.\n"
                + "What can I do for you?\n"
                + "———————————————————————\n";
        System.out.println(greeting);

        // Initialise WALLE's memory
        memory = new String[100];

        // Start process of echoing user input
        WALLE.interactUntilBye();

    }

    private static void interactUntilBye() {

        // Listen for first user input
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        // While input is not 'bye', echo input back to user
        while (!input.equals("bye")) {
            System.out.printf("\n   %s\n\n", input);
            input = scanner.nextLine();
        }

        // Print goodbye message
        System.out.println("\n  Bye bye! See you next time, I hope!\n");

        // Close scanner
        scanner.close();

    }

}
