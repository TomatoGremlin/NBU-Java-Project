package exeptions;

public class ItemHasExpiredException extends Exception {
    public ItemHasExpiredException(String message) {
        super(message);
    }
}
