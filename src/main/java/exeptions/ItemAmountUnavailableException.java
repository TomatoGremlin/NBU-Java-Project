package exeptions;

public class ItemAmountUnavailableException extends Exception {

    double availableAmount;
    public ItemAmountUnavailableException(String message, Double availableAmount) {
        super(message);
        this.availableAmount = availableAmount;
    }
}
