package Store;

import Store.People.Cashier;
import Store.People.Client;
import exeptions.ItemAmountUnavailableException;

import java.time.LocalDate;
import java.util.*;

public class Checkout {
    private Cashier cashier;
    private Queue<LinkedHashSet<Client>> clients ; // queue of linked hash set
    private HashSet<Receipt> receipts ;// hash set by id
    private Store store;

    public Checkout(Cashier cashier, List<Client> client, Store store) {
        this.cashier = cashier;
        this.clients = new LinkedList<>();
        this.receipts = new HashSet<>();
        this.store = store;
    }

    public double scanItems(Client client) throws ItemAmountUnavailableException {
        double sumOwed = 0;

        for (Map.Entry<Item, Integer> item: client.getItems( ).entrySet()) {

            if (!checkForAvailability( currentItem, itemAmount )){
                throw new ItemAmountUnavailableException( item.getName() + " only has "+ unitsAvailable +
                        " when " + itemAmount + " are needed", unitsAvailable);
            }
            sumOwed += currentItem.calculateSellingPrice();
        }
        return sumOwed;
    }
    public boolean canTransactionPass(Client client, double sumOwed){
        if (client.getBudget() >= sumOwed){
            return true;
            //TODO make a function that writes in a file the data for the items that take boolen value as parameter
            // and a function that reads the data to print it into the console(?)
        }
        return false;
    }
    // method for paying that has the transaction has passed method and the receipts are here + add itesm to sold items in store
    public boolean payingForItems(Client client, double sumOwed){
        if (canTransactionPass(client, sumOwed)){
            Receipt receipt;


           addItemsToSold(client);
           removeSoldItemsFromAvailable(client);
           return true;
        }
        return false;
    }

    public boolean giveReceipt(Client client, Receipt receipt){
        receipt = new Receipt(cashier, LocalDate.now().atStartOfDay(), client.getItems());
        return receipts.add(receipt);
    }

    public boolean addItemsToSold(Client client){
        for (Map.Entry<Item, Integer> item: client.getItems( ).entrySet()) {
            store.getSoldItemsList().put(item);
        }
        return true;
    }
    public boolean removeSoldItemsFromAvailable(Client client){

        store.getItemsAvailable().putAll(client.getItems());

        return true;
    }

    public int countInstances(Item item){
        int counter = 0;
        for (Item currentItem: store.getItemsAvailable() ) {
            if(currentItem.equals(item)){
                counter++;
            }
        }
        return counter;
    }

    public boolean checkForAvailability(Item item, int itemAmount) {
        int unitsAvailable = countInstances( item );
        if (unitsAvailable >= itemAmount){
            return true;
        }
        return  false;
    }
}

