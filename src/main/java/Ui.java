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
}
