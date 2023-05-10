package exeptions;

public class RegisterDoesNotExistException extends Exception {
    public RegisterDoesNotExistException(String message) {
        super(message);
    }
}
