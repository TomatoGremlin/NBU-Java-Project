package exeptions;

public class CashierAlreadyAssignedException extends Exception {
    public CashierAlreadyAssignedException(String message) {
        super(message);
    }
}
