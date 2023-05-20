package exeptions;

import java.math.BigDecimal;

public class IncorrectSalaryValueException extends IncorrectMoneyValueException {
    public IncorrectSalaryValueException(String message,  BigDecimal moneyValue) {
        super(message,  moneyValue);
    }
}
