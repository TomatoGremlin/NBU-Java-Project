package exeptions.moneyExceptions;

import java.math.BigDecimal;

public class IncorrectPriceValueException extends IncorrectMoneyValueException {
    public IncorrectPriceValueException(String message, BigDecimal moneyValue) {
        super(message,  moneyValue);
    }
}
