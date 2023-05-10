package Store;

import Store.People.Cashier;
import exeptions.CashierAlreadyAssignedException;
import exeptions.RegisterDoesNotExistException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Store {
    private int daysTillExpirationAllowed;
    private int percentageSale;
    private HashSet<Cashier>cashiersList; // hash set differentiating by id because otherwise it would be by referential
    private Map<Item, Double> itemsAvailable;
    private Map<Item, Double> soldItemsList;
    private HashSet<Register> registers;


    public Store( int daysTillExpirationAllowed, int percentageSale, HashSet<Cashier> cashiersList, Map<Item, Double> itemsAvailable ) {
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.cashiersList = cashiersList;
        this.itemsAvailable = itemsAvailable;
        this.soldItemsList = new HashMap<>();
        this.registers = new HashSet<>();
    }


    // Assign a cashier to a register
    public boolean assignCashier(Cashier cashier,  Register register) throws RegisterDoesNotExistException, CashierAlreadyAssignedException {
        if (registers.contains(register)){
            throw new RegisterDoesNotExistException("The register you have chosen doesn't exist");
        }
        if ( register.getCashier().equals(cashier) ){
            throw new CashierAlreadyAssignedException("The cashier you have chosen is already assigned to this register");
        }
        register.setCashier(cashier);
        return true;
    }

     // 1 . Calculate the sum of all the items sold
    public double calculateItemsSoldRevenue(){
        double sum = 0;
        for (Map.Entry<Item, Double> entry: soldItemsList.entrySet()) {
            sum += entry.getKey().calculateFinalSellingPrice() * entry.getValue();
        }
        return sum;
    }
    // 2. Calculate the costs for the salaries of the employees
     public double calculateCashiersSalaries(){
         double sum = 0;
         for ( Cashier currentCashier: cashiersList ) {
             sum += currentCashier.getMonthlySalary();
         }
         return sum;
     }


    // 3. Calculate the sum the delivery will cost
     public double calculateDeliveryCosts(){
         double sum = 0;
         for (Map.Entry<Item, Double> entry: soldItemsList.entrySet()) {
             sum += entry.getKey().getDeliveryPrice() * entry.getValue();
         }
         return sum;
     }

     // 4. Calculate the total revenue
    public double calculateTotalRevenue(){
        return calculateItemsSoldRevenue() - (calculateCashiersSalaries() + calculateDeliveryCosts());
    }


    // To return how many receipts there have been
    public int getNumberOfGivenReceipts(){
        int numReceipts = 0;
        for (Register register: registers) {
            numReceipts += register.getReceipts().size();
        }
         return numReceipts;
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

    public Map<Item, Double> getItemsAvailable() {
        return itemsAvailable;
    }

    public Map<Item, Double> getSoldItemsList() {
        return soldItemsList;
    }

    public HashSet<Register> getRegisters() {
        return registers;
    }


    @Override
    public String toString() {
        return "Store {" +
                "daysTillExpirationAllowed=" + daysTillExpirationAllowed +
                ", percentageSale=" + percentageSale +
                ", cashiersList=" + cashiersList +
                ", itemsAvailable=" + itemsAvailable +
                ", soldItemsList=" + soldItemsList +
                ", receiptsList=" + registers +
                '}';
    }
}
