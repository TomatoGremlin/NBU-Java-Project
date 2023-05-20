package exeptions;

import java.math.BigDecimal;

public class IncorrectClientBudgetException extends IncorrectMoneyValueException {
    public IncorrectClientBudgetException(String message,  BigDecimal moneyValue) {
        super(message,  moneyValue);
    }
}
