package Store;

import Store.People.Cashier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Store {
    private int daysTillExpirationAllowed;
    private int percentageSale;
    private HashSet<Cashier>cashiersList; // hash set differentiating by id because otherwise it would be by referential
    private Map<Item, Integer> itemsAvailable;
    private Map<Item, Integer> soldItemsList;
    private HashSet<Checkout> receiptsList; // hash set


    public Store( int daysTillExpirationAllowed, int percentageSale, HashSet<Cashier> cashiersList, Map<Item, Integer> itemsAvailable ) {
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.cashiersList = cashiersList;
        this.itemsAvailable = itemsAvailable;
        this.soldItemsList = new HashMap<>();
        this.receiptsList = new HashSet<>();
    }

    public double calculateTotalRevenue(){
        return calculateItemsSoldRevenue() - (calculateCashiersSalaries() + calculateDeliveryCosts());
    }
     public double calculateCashiersSalaries(){
         double sum = 0;
         for ( Cashier currentCashier: cashiersList ) {
             sum += currentCashier.getMonthlySalary();
         }
         return sum;
     }
     public double calculateDeliveryCosts(){
         double sum = 0;
         for (Map.Entry<Item, Integer> entry: soldItemsList.entrySet()) {
             sum += entry.getKey().getDeliveryPrice() * entry.getValue();
         }
         return sum;
     }
    public double calculateItemsSoldRevenue(){
        double sum = 0;
        for (Map.Entry<Item, Integer> entry: soldItemsList.entrySet()) {
            sum += entry.getKey().calculateSellingPrice() * entry.getValue();
        }
        return sum;
    }


    public int getDaysTillExpirationAllowed() {
        return daysTillExpirationAllowed;
    }

    public int getPercentageSale() {
        return percentageSale;
    }

    public HashSet<Cashier> getCashiersList() {
        return cashiersList;
    }

    public Map<Item, Integer> getItemsAvailable() {
        return itemsAvailable;
    }

    public Map<Item, Integer> getSoldItemsList() {
        return soldItemsList;
    }

    public HashSet<Checkout> getReceiptsList() {
        return receiptsList;
    }


    @Override
    public String toString() {
        return "Store {" +
                "daysTillExpirationAllowed=" + daysTillExpirationAllowed +
                ", percentageSale=" + percentageSale +
                ", cashiersList=" + cashiersList +
                ", itemsAvailable=" + itemsAvailable +
                ", soldItemsList=" + soldItemsList +
                ", receiptsList=" + receiptsList +
                '}';
    }
}
