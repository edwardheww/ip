public class InvalidTaskTypeException extends WALLEException {

    public InvalidTaskTypeException(String taskType) {
        super("\n   ERROR :( Sorry...what is a " + taskType + "? That's not in my dictionary...\n");
    }

}
