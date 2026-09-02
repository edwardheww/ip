package walle.ui;

import java.util.ArrayList;
import java.util.Scanner;

import walle.exception.WALLEException;
import walle.task.Task;

/**
 * Handles all interactions with the user: reading commands from standard
 * input, and printing WALLE's greetings, updates, and error messages.
 */
public class Ui {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads the next line of user input.
     *
     * @return the raw command string entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Closes the input scanner. Should be called once, when WALLE shuts down.
     */
    public void closeScanner() {
        scanner.close();
    }

    /**
     * Prints WALLE's greeting when the program starts.
     */
    public void greetUser() {
        String greeting = "\n"
                + "Hey! I'm WALLE.\n"
                + "What can I do for you?\n"
                + "———————————————————————\n";
        System.out.println(greeting);
    }

    /**
     * Prints WALLE's farewell message when the user exits.
     */
    public void bidFarewell() {
        System.out.println("\n  Bye bye! See you next time, I hope!\n");
    }

    /**
     * Prints an error message for a generic (non-WALLEException) exception.
     *
     * @param e the exception that was thrown.
     */
    public void printErrorMsg(Exception e) {
        System.out.println("\n    ERROR: " + e.getMessage() + " :(\n");
    }

    /**
     * Prints an error message for a WALLEException, whose message is already
     * formatted for direct display to the user.
     *
     * @param e the WALLEException that was thrown.
     */
    public void printErrorMsg(WALLEException e) {
        System.out.println(e.getMessage());
    }

    /**
     * Prints a confirmation after a task has been deleted.
     *
     * @param deletedTask the task that was removed.
     * @param tasksLeft   the number of tasks remaining in the list.
     */
    public void printTaskDeletionUpdate(Task deletedTask, int tasksLeft) {
        System.out.println("\n    Got it! I've removed the task:\n"
                + "        " + deletedTask
                + "\n    " + tasksLeft + " task(s) left, let's go :D\n");
    }

    /**
     * Prints a confirmation after a task has been added.
     *
     * @param newTask  the task that was added.
     * @param numTasks the total number of tasks now in the list.
     */
    public void printTaskAdditionUpdate(Task newTask, int numTasks) {
        System.out.println("\n    Got it! I've added the task:");
        System.out.printf("        %s\n", newTask);
        System.out.printf("    Now you have %d task(s) on your list!\n\n", numTasks);
    }

    /**
     * Prints every task currently in the given list, numbered from 1.
     *
     * @param taskList the list of tasks to display.
     */
    public void listTasks(ArrayList<Task> taskList) {
        System.out.println("\n  Here are the tasks in your list:"); // Used for newline
        for (int pos = 0; pos < taskList.size(); pos++) {
            System.out.println("   "
                    + (pos + 1)
                    + "."
                    + taskList.get(pos));
        }
        System.out.println(""); // Used for newline
    }

    /**
     * Prints a confirmation after a task has been marked as done.
     *
     * @param markedTask the task that was marked.
     */
    public void printTaskMarkedUpdate(Task markedTask) {
        System.out.printf("\n    Nice! I've marked this task as done:\n"
                + "        %s\n\n", markedTask);
    }

    /**
     * Prints a confirmation after a task has been unmarked as done.
     *
     * @param unmarkedTask the task that was unmarked.
     */
    public void printTaskUnmarkedUpdate(Task unmarkedTask) {
        System.out.printf("\n    OK, I've marked this task as not done yet:\n"
                + "        %s\n\n", unmarkedTask);
    }

}
