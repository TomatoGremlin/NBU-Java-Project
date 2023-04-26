package Store;

import Store.People.Cashier;
import Store.People.Client;
import exeptions.ItemAmountUnavailableException;

import java.util.List;

public class Checkout {
    private Cashier cashier;
    private Client client;
    private List<Item>items;
    private Store store;



    private int numberOfReceipts;
    private double moneyGained;

    public Checkout(Cashier cashier, Client client, List<Item> items, Store store) {
        this.cashier = cashier;
        this.client = client;
        this.items = items;
        this.store = store;
    }

    public double scanItems(int itemAmount) throws ItemAmountUnavailableException {
        double sumOwed = 0;
        for (Item currentItem: items ) {

            if (checkForAvailability(currentItem, itemAmount )){
                sumOwed += currentItem.calculateSellingPrice();
            }
        }
        return sumOwed;
    }
    public boolean hasTransactionPassed(double sumOwed){
        if (client.budget >= sumOwed){
            return true;
            //TODO make a function that writes in a file the data for the items that take boolen value as parameter
            // and a function that reads the data to print it into the console(?)
        }else {
            return false;
        }
    }


    public int countInstances(Item item){
        int counter = 0;
        for (Item currentItem: store.itemsAvailable ) {
            if(currentItem.equals(item)){
                counter++;
            }
        }
        return counter;
    }

    public boolean checkForAvailability(Item item, int itemAmount) throws ItemAmountUnavailableException{
        int unitsAvailable = countInstances( item );
        if (unitsAvailable >= itemAmount){
            return true;
        }
        else{
            throw new ItemAmountUnavailableException( item.name + " only has "+ unitsAvailable +
                    " when " + itemAmount + " are needed", unitsAvailable);
        }
    }
}
