package Store.Interfaces;

import Store.Item;
import Store.People.Client;
import exeptions.NoItemsAvailableException;

import java.math.BigDecimal;
import java.util.Map;

public interface ItemsServices {
    public Map<Item, BigDecimal> addItemsToSold(Client client);
    public Map<Item, BigDecimal> removeSoldItemsFromAvailable(Client client) throws NoItemsAvailableException;
}
