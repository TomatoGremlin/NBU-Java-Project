package exeptions;

public class ItemAmountUnavailableException extends Exception {

    int availableAmount;
    public ItemAmountUnavailableException(String message, int availableAmount) {
        super(message);
        this.availableAmount = availableAmount;
    }
}
