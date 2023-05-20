package exeptions;

import java.math.BigDecimal;

public class ItemAmountUnavailableException extends Exception {

    BigDecimal availableAmount;
    public ItemAmountUnavailableException(String message, BigDecimal availableAmount) {
        super(message);
        this.availableAmount = availableAmount;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }
}
