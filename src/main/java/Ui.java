public class Ui {

    // Greets user
    public void greetUser() {
        String greeting = "\n"
                + "Hey! I'm WALLE.\n"
                + "What can I do for you?\n"
                + "———————————————————————\n";
        System.out.println(greeting);
    }

    // Bids user farewell
    public void bidFarewell() {
        System.out.println("\n  Bye bye! See you next time, I hope!\n");
    }

    // Prints error message for non-WALLEException exceptions
    public void printErrorMsg(Exception e) {
        System.out.println("\n    ERROR: " + e.getMessage() + " :(\n");
    }

    // Prints error message for WALLEExceptions
    public void printErrorMsg(WALLEException e) {
        System.out.println(e.getMessage());
    }

    // Updates user after task deletion
    public void printTaskDeletionUpdate(Task deletedTask, int tasksLeft) {
        System.out.println("\n    Got it! I've removed the task:\n"
                + "        " + deletedTask
                + "\n    " + tasksLeft + " task(s) left, let's go :D\n");
    }

    // Updates user after task addition
    public void printTaskAdditionUpdate(Task newTask, int numTasks) {
        System.out.println("\n    Got it! I've added the task:");
        System.out.printf("        %s\n", newTask);
        System.out.printf("    Now you have %d task(s) on your list!\n\n", numTasks);
    }

}
