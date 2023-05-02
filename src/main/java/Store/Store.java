package Store;

import Store.People.Cashier;

import java.util.List;

public class Store {
    private int daysTillExpirationAllowed;
    private int percentageSale;
    private List<Item>itemsAvailable;
    private List<Cashier>cashiersList;
    private List<Item>soldItemsList;
    private List<Receipt>receiptsList;

    public Store(int daysTillExpirationAllowed, int percentageSale, List<Item> itemsAvailable) {
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.itemsAvailable = itemsAvailable;
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
