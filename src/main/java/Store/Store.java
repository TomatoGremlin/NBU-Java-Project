package Store;

import Store.People.Cashier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Store {
    private int daysTillExpirationAllowed;
    private int percentageSale;
    private HashSet<Cashier>cashiersList; // hash set + da se napravi da se razlichavat po primerno id zashtoto inache shte e po referenciq
    private Map<Item, Integer> itemsAvailable;
    private Map<Item, Integer> soldItemsList;
    private HashSet<Checkout> receiptsList; // hash set


    public Store(int daysTillExpirationAllowed, int percentageSale, HashSet<Cashier> cashiersList, Map<Item, Integer> itemsAvailable) {
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
         for ( Cashier currentCashier:cashiersList ) {
             sum += currentCashier.getMonthlySalary();
         }
         return sum;
     }
     public double calculateDeliveryCosts(){
         double sum = 0;
         for ( Item currentItem: soldItemsList ) {
             sum += currentItem.getDeliveryPrice();
         }
         return sum;
     }
    public double calculateItemsSoldRevenue(){
        double sum = 0;
        for ( Item currentItem: soldItemsList ) {
            sum += currentItem.calculateSellingPrice();
        }
        return sum;
    }



    public int getDaysTillExpirationAllowed() {
        return daysTillExpirationAllowed;
    }

    public int getPercentageSale() {
        return percentageSale;
    }

    public List<Item> getItemsAvailable() {
        return itemsAvailable;
    }

    public List<Cashier> getCashiersList() {
        return cashiersList;
    }

    public List<Item> getSoldItemsList() {
        return soldItemsList;
    }

    public List<Receipt> getReceiptsList() {
        return receiptsList;
    }
}
