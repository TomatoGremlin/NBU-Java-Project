package Store.Interfaces.registerInterfaces;

import Store.Item;
import Store.People.Client;
import exeptions.moneyExceptions.IncorrectClientBudgetException;
import exeptions.ItemAmountInsufficientException;
import exeptions.ItemHasExpiredException;
import exeptions.NoItemsAvailableException;

import java.math.BigDecimal;

public interface TransactionServices extends ItemsServices {
    public boolean checkForAvailability(Item item, BigDecimal itemAmount) ;
    public BigDecimal scanItems(Client client) throws ItemAmountInsufficientException, ItemHasExpiredException;
    public boolean canTransactionPass(Client client, BigDecimal sumOwed);
    public boolean finalizeTransaction(Client client, BigDecimal sumOwed) throws IncorrectClientBudgetException, NoItemsAvailableException;

}
