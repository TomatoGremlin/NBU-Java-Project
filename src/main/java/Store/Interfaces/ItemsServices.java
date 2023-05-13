package Store.Interfaces;

import Store.People.Client;

public interface ItemsServices {
    public boolean addItemsToSold(Client client);
    public boolean removeSoldItemsFromAvailable(Client client);
}
