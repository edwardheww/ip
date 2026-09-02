package walle.exceptions;

public class MissingArgException extends WALLEException {

    public MissingArgException(String taskType, String argName) {
        super("\n   ERROR :( Your " + taskType + " is missing the " + argName
                + " argument. That's not allowed!\n");
    }

}
