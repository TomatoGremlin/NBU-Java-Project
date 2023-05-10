package Store;

import Store.People.Cashier;
import Store.People.Client;
import Utils.ioReceipt;
import exeptions.ItemAmountUnavailableException;

import java.util.*;
// dostavka, smqtane
public class Register {
    private static int num_instances = 0;
    private int registerNumber;
    private Cashier cashier;
    private Queue<Client> clients ; // queue ( to keep the order of enqueuing) of linked hash set ( to have no repetitions of client )
    private HashSet<Receipt> receipts ;// hash set differentiating by id
    private Store store;


    public Register(Cashier cashier, List<Client> client, Store store) {
        num_instances++;
        registerNumber = num_instances;

        this.cashier = cashier;
        this.clients = new ArrayDeque<>();
        this.receipts = new HashSet<>();
        this.store = store;
    }

    // -----  Client Queue operations  -----
    public boolean addClient(Client newClient) {
        if ( !clients.contains(newClient) ) {
            return clients.add(newClient);
        }

        return false;
    }

    public boolean removeClient(Client client) {
        if ( clients.contains(client) ) {
            return clients.remove(client);
        }
        return false;
    }

    // ----  Making a Transaction operations  -----

    // 1. Check if there is enough units of the item the client wants
    public boolean checkForAvailability(Item item, double itemAmount) {
        Double unitsAvailable = store.getItemsAvailable().get(item) ;
        if (unitsAvailable >= itemAmount){
            return true;
        }
        return false;
    }

    // 2. Calculate the sum of the transaction - throw exception if one of the items has less than enough units
    public double scanItems(Client client) throws ItemAmountUnavailableException {
        double sumOwed = 0;

        for (Map.Entry<Item, Double> entry: client.getItems( ).entrySet()) {

            if (!checkForAvailability( entry.getKey(), entry.getValue() )){
                throw new ItemAmountUnavailableException( entry.getKey().getName() + " only has "+ entry.getValue() +
                        " when " + entry.getValue() + " are needed", entry.getValue());
            }

            sumOwed += entry.getKey().calculateFinalSellingPrice() * entry.getValue();
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

        for (Map.Entry<Item, Double> entry: client.getItems().entrySet()) {
            Item item = entry.getKey();
            double quantity = entry.getValue();

            if (store.getSoldItemsList().containsKey(item)) {

                double currentQuantity = store.getSoldItemsList().get(item);
                double newQuantity = currentQuantity + quantity;
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
            double updatedQuantity = store.getItemsAvailable().get(item) - client.getItems().get(item);
            store.getItemsAvailable().put( item, updatedQuantity );
        }
        return true;
    }


    // 7. The client pays and the transaction is complete
    public boolean finalizeTransaction(Client client, double sumOwed){

        if ( canTransactionPass(client, sumOwed) ){
            Receipt receipt = new Receipt(cashier, client.getItems());
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


    public Cashier getCashier() {
        return cashier;
    }

    public Queue<Client> getClients() {
        return clients;
    }

    public HashSet<Receipt> getReceipts() {
        return receipts;
    }


    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Register register = (Register) o;
        return registerNumber == register.registerNumber && Objects.equals(store, register.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registerNumber, store);
    }
}

