package Store.Interfaces;

import Store.Item;
import Store.People.Client;
import exeptions.IncorrectClientBudgetException;
import exeptions.ItemAmountUnavailableException;
import exeptions.ItemHasExpiredException;
import exeptions.NoItemsAvailableException;

import java.math.BigDecimal;

public interface TransactionServices extends ItemsServices {
    public boolean checkForAvailability(Item item, BigDecimal itemAmount) ;
    public BigDecimal scanItems(Client client) throws ItemAmountUnavailableException, ItemHasExpiredException;
    public boolean canTransactionPass(Client client, BigDecimal sumOwed);
    public boolean finalizeTransaction(Client client, BigDecimal sumOwed) throws IncorrectClientBudgetException, NoItemsAvailableException;

}
