package exeptions;

import Store.People.Cashier;

public class CashierUnavailableException extends Exception {
    Cashier cashier;

    public CashierUnavailableException(String message, Cashier cashier) {
        super(message);
        this.cashier = cashier;
    }

    public Cashier getCashier() {
        return cashier;
    }
}
