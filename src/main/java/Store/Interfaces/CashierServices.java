package Store.Interfaces;

import Store.People.Cashier;
import Store.Register;
import exeptions.CashierAlreadyAssignedException;
import exeptions.RegisterDoesNotExistException;

public interface CashierServices {
    public boolean assignCashier(Cashier cashier, Register register) throws RegisterDoesNotExistException, CashierAlreadyAssignedException ;
}
