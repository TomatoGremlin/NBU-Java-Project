package Store.Interfaces.registerInterfaces;

import Store.People.Client;

public interface ClientQueueServices {

    public boolean addClient(Client newClient);

    public boolean removeClient(Client client);

}
