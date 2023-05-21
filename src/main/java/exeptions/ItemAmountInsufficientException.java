package exeptions;

import java.math.BigDecimal;

public class ItemAmountInsufficientException extends Exception {

    BigDecimal availableAmount;
    public ItemAmountInsufficientException(String message, BigDecimal availableAmount) {
        super(message);
        this.availableAmount = availableAmount;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }
}
