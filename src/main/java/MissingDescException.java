public class MissingDescException extends WALLEException {

    public MissingDescException(String taskType) {
        super("\n   ERROR :( Your " + taskType + " has an empty description. That's not allowed!\n");
    }

}
