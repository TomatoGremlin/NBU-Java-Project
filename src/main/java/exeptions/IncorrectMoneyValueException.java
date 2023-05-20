package exeptions;

import java.math.BigDecimal;

public class IncorrectMoneyValueException extends Exception {
    private BigDecimal moneyValue;

    public IncorrectMoneyValueException(String message, BigDecimal moneyValue) {
        super(message);
        this.moneyValue = moneyValue;
    }

    public BigDecimal getMoneyValue() {
        return moneyValue;
    }
}
