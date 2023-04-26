package Store;

import Store.People.Cashier;

import java.util.List;

public class Store {
    public int daysTillExpirationAllowed;
    public int percentageSale;
    public List<Item>itemsAvailable;
    public List<Cashier>cashiersList;
    public List<Item>soldItemsList;
    private List<Receipt>receiptsList;

    public Store(int daysTillExpirationAllowed, int percentageSale, List<Item> itemsAvailable) {
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.itemsAvailable = itemsAvailable;
    }

    // double calculateTotalRevenue(){}
    // double calculateCashiersSalaries(){}
    // double calculateDeliveryCosts(){}
    // double calculateItemsSoldMoney(){}
}
