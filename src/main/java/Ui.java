public class Ui {

    // Greets User
    public void greetUser() {
        String greeting = "\n"
                + "Hey! I'm WALLE.\n"
                + "What can I do for you?\n"
                + "———————————————————————\n";
        System.out.println(greeting);
    }

    // Prints error message for non-WALLEException exceptions
    public void printErrorMsg(Exception e) {
        System.out.println("\n    ERROR: " + e.getMessage() + " :(\n");
    }

    // Prints error message for WALLEExceptions
    public void printErrorMsg(WALLEException e) {
        System.out.println(e.getMessage());
    }

    // Prints status message after task deletion
    public void printPostDeletionUpdate(Task deletedTask, int tasksLeft) {
        System.out.println("\n    Got it! I've removed the task:\n"
                + "        " + deletedTask
                + "\n    " + tasksLeft + " task(s) left, let's go :D\n");
    }

}
