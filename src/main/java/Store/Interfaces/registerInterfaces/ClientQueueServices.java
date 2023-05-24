package Store.Interfaces.registerInterfaces;

import Store.People.Client;

public interface ClientQueueServices {

    public boolean enqueueClient(Client newClient);

    public boolean dequeueClient(Client client);

}
