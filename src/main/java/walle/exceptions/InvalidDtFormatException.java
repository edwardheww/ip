package walle.exceptions;

public class InvalidDtFormatException extends WALLEException {

    public InvalidDtFormatException() {
        super("\n   ERROR :( Please use the datetime format yyyy-MM-dd HHmm\n");
    }

}
