package Store;

import Store.Interfaces.registerInterfaces.ItemsServices;
import Store.Interfaces.registerInterfaces.ClientQueueServices;
import Store.Interfaces.registerInterfaces.ReceiptServices;
import Store.Interfaces.registerInterfaces.TransactionServices;
import Store.People.Cashier;
import Store.People.Client;
import Utils.IOreceipt;
import exeptions.moneyExceptions.IncorrectClientBudgetException;
import exeptions.ItemAmountInsufficientException;
import exeptions.ItemHasExpiredException;
import exeptions.NoItemsAvailableException;

import java.math.BigDecimal;
import java.util.*;

public class Register implements TransactionServices, ClientQueueServices, ItemsServices, ReceiptServices {
    private static int num_instances = 0;
    private final int registerNumber;
    private Cashier cashier;
    private Queue<Client> clients ; // queue ( to keep the order of enqueuing) of linked hash set ( to have no repetitions of client )
    private HashSet<Receipt> receipts ; // hash set differentiating by id
    private Store store;

    public Register(Cashier cashier, Queue<Client> clients, Store store , HashSet<Receipt> receipts ) {
        num_instances++;
        registerNumber = num_instances;

        this.cashier = cashier;
        this.clients = clients;
        this.receipts = receipts;
        this.store = store;
    }

    public Register(Cashier cashier, Store store , HashSet<Receipt> receipts ) {
        num_instances++;
        registerNumber = num_instances;

        this.cashier = cashier;
        this.clients = new ArrayDeque<>();
        this.receipts = receipts;
        this.store = store;
    }

    public Register(Cashier cashier, Queue<Client> clients, Store store) {
        num_instances++;
        registerNumber = num_instances;

        this.cashier = cashier;
        this.clients = clients;
        this.receipts = new HashSet<>();
        this.store = store;
    }


    public Register(Cashier cashier, Store store) {
        num_instances++;
        registerNumber = num_instances;

        this.cashier = cashier;
        this.clients = new ArrayDeque<>();
        this.receipts = new HashSet<>();
        this.store = store;
    }

    public Register( Store store) {
        num_instances++;
        registerNumber = num_instances;

        this.cashier = null;
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
    public boolean checkForAvailability(Item item, BigDecimal itemAmount) {
        BigDecimal unitsAvailable = store.getItemsAvailable().get(item) ;
        //    if (unitsAvailable < itemAmount) - false
        return unitsAvailable.compareTo(itemAmount) >= 0;
    }

    // 2. Calculate the sum of the transaction - throw exception if one of the items has less than enough units

    // remove expired item from the client's items
    public BigDecimal removeExpiredItem(Item item, Client client){
        return client.getItems().remove(item);
    }

    @Override
    public BigDecimal scanItems(Client client) throws ItemAmountInsufficientException, ItemHasExpiredException {
        BigDecimal sumOwed = BigDecimal.valueOf(0);

        for (Map.Entry<Item, BigDecimal> entry: client.getItems( ).entrySet()) {

            if( !entry.getKey().isSellable() ) {
                throw new ItemHasExpiredException("Item cannot be sold because it has expired", entry.getKey());
            }
            if (!checkForAvailability( entry.getKey(), entry.getValue() )){
                throw new ItemAmountInsufficientException( entry.getKey().getName() + " only has " + entry.getValue() +
                        " when " + entry.getValue() + " are needed", entry.getValue());
            }

            BigDecimal price = entry.getKey().calculateFinalSellingPrice();
            BigDecimal unites =  entry.getValue() ;

            sumOwed = sumOwed.add(  price.multiply( unites )  );
        }
        return sumOwed;
    }

    // 3. Check if the client has enough money to pay the sum
    @Override
    public boolean canTransactionPass(Client client, BigDecimal sumOwed){
        // if client.getBudget() < sumOwed - false
        return client.getBudget().compareTo(sumOwed) >= 0;
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
    public Map<Item, BigDecimal> addItemsToSold(Client client){

        for (Map.Entry<Item, BigDecimal> entry: client.getItems().entrySet()) {
            Item item = entry.getKey();
            BigDecimal quantity = entry.getValue();

            if (store.getSoldItemsList().containsKey(item)) {
                BigDecimal currentQuantity = store.getSoldItemsList().get(item);
                quantity = currentQuantity.add( quantity );
            }
            store.getSoldItemsList().put( item, quantity );
        }
        return store.getSoldItemsList();
    }

    // 6.Update the available unites of each item sold
    @Override
    public Map<Item, BigDecimal> removeSoldItemsFromAvailable(Client client) throws NoItemsAvailableException {
        if (store.getItemsAvailable().isEmpty() || client.getItems().isEmpty()){
            throw new NoItemsAvailableException("Either the store's inventory or the client's shopping cart is empty");
        }

        client.getItems().keySet().forEach( item -> {
            BigDecimal updatedQuantity = store.getItemsAvailable().get(item). subtract( client.getItems().get(item) );
            store.getItemsAvailable().put(item, updatedQuantity);
        });
        return store.getItemsAvailable();
    }


    // 7. The client pays and the transaction is complete
    @Override
    public boolean finalizeTransaction(Client client, BigDecimal sumOwed) throws IncorrectClientBudgetException, NoItemsAvailableException {

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

    public void setReceipts(HashSet<Receipt> receipts) {
        this.receipts = receipts;
    }

    public int getRegisterNumber() {return registerNumber; }

    public Store getStore() {return store;}

    public void setStore(Store store) {
        this.store = store;
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

