package Store;

import Store.Interfaces.ClientQueueServices;
import Store.Interfaces.ReceiptServices;
import Store.Interfaces.TransactionServices;
import Store.People.Cashier;
import Store.People.Client;
import Utils.IOreceipt;
import exeptions.IncorrectClientBudgetException;
import exeptions.ItemAmountUnavailableException;
import exeptions.ItemHasExpiredException;

import java.math.BigDecimal;
import java.util.*;

public class Register implements TransactionServices, ClientQueueServices, ReceiptServices {
    private static int num_instances = 0;
    private int registerNumber;
    private Cashier cashier;
    private Queue<Client> clients ; // queue ( to keep the order of enqueuing) of linked hash set ( to have no repetitions of client )
    private HashSet<Receipt> receipts ; // hash set differentiating by id
    private Store store;

    public Register(Cashier cashier, Store store) {
        num_instances++;
        registerNumber = num_instances;

        this.cashier = cashier;
        this.clients = new ArrayDeque<>();
        this.receipts = new HashSet<>();
        this.store = store;
    }


    public Register(Cashier cashier, List<Client> client, Store store) {
        num_instances++;
        registerNumber = num_instances;

        this.cashier = cashier;
        this.clients = new ArrayDeque<>();
        this.receipts = new HashSet<>();
        this.store = store;
    }


    // -----  Client Queue operations  -----
    @Override
    public boolean addClient(Client newClient) {
        if ( !clients.contains(newClient) ) {
            return clients.add(newClient);
        }
        return false;
    }
    @Override
    public boolean removeClient(Client client) {
        if ( clients.contains(client) ) {
            return clients.remove(client);
        }
        return false;
    }

    // ----  Making a Transaction operations  -----

    // 1. Check if there is enough units of the item the client wants
    @Override
    public boolean checkForAvailability(Item item, double itemAmount) {
        Double unitsAvailable = store.getItemsAvailable().get(item) ;
        if (unitsAvailable >= itemAmount){
            return true;
        }
        return false;
    }

    // 2. Calculate the sum of the transaction - throw exception if one of the items has less than enough units

    // remove expired item from the client's items
    public boolean removeExpiredItem(Item item, Client client){
        client.getItems().remove(item);
        return true;
    }

    @Override
    public BigDecimal scanItems(Client client) throws ItemAmountUnavailableException, ItemHasExpiredException {
        BigDecimal sumOwed = BigDecimal.valueOf(0);

        for (Map.Entry<Item, Double> entry: client.getItems( ).entrySet()) {

            if (!checkForAvailability( entry.getKey(), entry.getValue() )){
                throw new ItemAmountUnavailableException( entry.getKey().getName() + " only has " + entry.getValue() +
                        " when " + entry.getValue() + " are needed", entry.getValue());
            }
            if( !entry.getKey().isSellable() ) {
                removeExpiredItem(entry.getKey(), client);
                throw new ItemHasExpiredException("Item cannot be sold because it has expired");
            }

            BigDecimal price = entry.getKey().calculateFinalSellingPrice();
            BigDecimal unites = BigDecimal.valueOf( entry.getValue() );

            sumOwed = sumOwed.add(  price.multiply( unites )  );
        }
        return sumOwed;
    }

    // 3. Check if the client has enough money to pay the sum
    @Override
    public boolean canTransactionPass(Client client, BigDecimal sumOwed){
        // if client.getBudget() < sumOwed
        if (client.getBudget().compareTo( sumOwed ) == -1 ){  return false; }
        return true;
    }

    // 4. Give the client a Receipt
    @Override
    public boolean addReceipt(Receipt receipt){
        return receipts.add(receipt);
    }

    @Override
    public void showReceipt( Receipt receipt ){
        System.out.println(  IOreceipt.readReceipt("RECEIPTS/receipt#" + receipt.getId_number())  );
    }


    // 5. Update the store inventory of items that have been sold
    @Override
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
    @Override
    public boolean removeSoldItemsFromAvailable(Client client){
        for (Item item : client.getItems().keySet()) {
            double updatedQuantity = store.getItemsAvailable().get(item) - client.getItems().get(item);
            store.getItemsAvailable().put( item, updatedQuantity );
        }
        return true;
    }


    // 7. The client pays and the transaction is complete
    @Override
    public boolean finalizeTransaction(Client client, BigDecimal sumOwed) throws IncorrectClientBudgetException {

        if ( canTransactionPass(client, sumOwed) ){
            Receipt receipt = new Receipt(cashier, client.getItems());
            addReceipt(receipt);

            IOreceipt.writeReceipt("RECEIPTS/receipt#" + receipt.getId_number() , receipt);
            showReceipt(receipt);

            client.setBudget( client.getBudget().subtract(sumOwed) );

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

    public int getRegisterNumber() {return registerNumber; }

    public Store getStore() {return store;}

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

