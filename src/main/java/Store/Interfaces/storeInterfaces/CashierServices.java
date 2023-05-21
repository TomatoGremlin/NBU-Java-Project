package Store.Interfaces.storeInterfaces;

import Store.People.Cashier;
import Store.Register;
import exeptions.CashierUnavailableException;
import exeptions.RegisterUnavailableException;

public interface CashierServices {
    public boolean assignCashier(Cashier cashier, Register register) throws RegisterUnavailableException, CashierUnavailableException;
}
