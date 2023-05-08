package Store;

import Store.People.Cashier;
import Store.People.Client;
import Utils.ioReceipt;
import exeptions.ItemAmountUnavailableException;

import java.time.LocalDate;
import java.util.*;

public class Checkout {
    private Cashier cashier;
    private Queue<LinkedHashSet<Client>> clients ; // queue ( to keep the order of enqueuing) of linked hash set ( to have no repetitions of client )
    private HashSet<Receipt> receipts ;// hash set differentiating by id
    private Store store;

    public Checkout(Cashier cashier, List<Client> client, Store store) {
        this.cashier = cashier;
        this.clients = new LinkedList<>();
        this.receipts = new HashSet<>();
        this.store = store;
    }

    // -----  Client Queue operations  -----
    public boolean addClient(Client newClient) {
        if ( clients.isEmpty() ) {
            LinkedHashSet<Client> newSet = new LinkedHashSet<>();
            newSet.add(newClient);
            return clients.offer(newSet);
        } else {
            return clients.peek().add(newClient);
        }
    }

    public boolean removeClient(Client client) {
        if ( clients.contains(client) ) {
            return clients.remove(client);
        }
        return false;
    }

    // ----  Making a Transaction operations  -----

    // 1. Check if there is enough units of the item the client wants
    public boolean checkForAvailability(Item item, int itemAmount) {
        int unitsAvailable = store.getItemsAvailable().get(item) ;
        if (unitsAvailable >= itemAmount){
            return true;
        }
        return false;
    }

    // 2. Calculate the sum of the transaction - throw exception if one of the items has less than enough units
    public double scanItems(Client client) throws ItemAmountUnavailableException {
        double sumOwed = 0;

        for (Map.Entry<Item, Integer> entry: client.getItems( ).entrySet()) {

            if (!checkForAvailability( entry.getKey(), entry.getValue() )){
                throw new ItemAmountUnavailableException( entry.getKey().getName() + " only has "+ entry.getValue() +
                        " when " + entry.getValue() + " are needed", entry.getValue());
            }

            sumOwed += entry.getKey().calculateSellingPrice() * entry.getValue();
        }
        return sumOwed;
    }

    // 3. Check if the client has enough money to pay the sum
    public boolean canTransactionPass(Client client, double sumOwed){
        if (client.getBudget() >= sumOwed){  return true; }
        return false;
    }

    // 4. Give the client a Receipt
    public boolean addReceipt(Receipt receipt){
        return receipts.add(receipt);
    }
    public void printReceipt(Receipt receipt){
        ioReceipt.writeReceipt("RECEIPTS/receipt#" + receipt.getId_number() , receipt);
    }
    public void showReceipt( Receipt receipt ){
        System.out.println(  ioReceipt.readReceipt("RECEIPTS/receipt#" + receipt.getId_number())  );
    }


    // 5. Update the store inventory of items that have been sold
    public boolean addItemsToSold(Client client){

        for (Map.Entry<Item, Integer> entry: client.getItems().entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();

            if (store.getSoldItemsList().containsKey(item)) {
                int currentQuantity = store.getSoldItemsList().get(item);
                int newQuantity = currentQuantity + quantity;
                store.getSoldItemsList().put( item, newQuantity );
            } else {
                store.getSoldItemsList().put( item, quantity );
            }
        }
        return true;
    }
    // 6.Update the available unites of each item sold
    public boolean removeSoldItemsFromAvailable(Client client){
        for (Item item : client.getItems().keySet()) {
            int updatedQuantity = store.getItemsAvailable().get(item) - client.getItems().get(item);
            store.getItemsAvailable().put( item, updatedQuantity );
        }
        return true;
    }


    // 7. The client pays and the transaction is complete
    public boolean finalizeTransaction(Client client, double sumOwed){

        if ( canTransactionPass(client, sumOwed) ){
            Receipt receipt = new Receipt(cashier, LocalDate.now().atStartOfDay(), client.getItems());
            addReceipt(receipt);
            printReceipt(receipt);
            showReceipt(receipt);

            addItemsToSold(client);
            removeSoldItemsFromAvailable(client);

            removeClient(client);
            return true;
        }
        return false;
    }

}

