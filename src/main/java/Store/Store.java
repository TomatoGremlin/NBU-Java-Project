package Store;

import Store.Interfaces.CashierServices;
import Store.Interfaces.RevenueServices;
import Store.People.Cashier;
import Store.enums.ItemCategory;
import exeptions.CashierUnavailableException;
import exeptions.RegisterUnavailableException;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Store implements RevenueServices, CashierServices {
    private String name;
    private int daysTillExpirationAllowed;
    private int percentageSale;
    private HashSet<Cashier> cashiersList; // hash set differentiating by id because otherwise it would be by referential
    private Map<Item, Double> itemsAvailable;
    private Map<Item, Double> soldItemsList;
    private HashSet<Register> registers;

    private EnumMap<ItemCategory, BigDecimal> overchargeByCategory;


    public Store(String name, int daysTillExpirationAllowed, int percentageSale, HashSet<Cashier> cashiersList, Map<Item, Double> itemsAvailable, Map<Item, Double> soldItemsList, HashSet<Register> registers) {
        this.name = name;
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.cashiersList = cashiersList;
        this.itemsAvailable = itemsAvailable;
        this.soldItemsList = soldItemsList;
        this.registers = registers;

        this.overchargeByCategory = new EnumMap<>(ItemCategory.class);
    }

    public Store(String name , int daysTillExpirationAllowed, int percentageSale ) {
        this.name = name;
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.itemsAvailable = new HashMap<>();
        this.overchargeByCategory = new EnumMap<>(ItemCategory.class);
    }
    public Store( String name , int daysTillExpirationAllowed, int percentageSale, HashSet<Cashier> cashiersList, Map<Item, Double> itemsAvailable ) {
        this.name = name;
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.cashiersList = cashiersList;
        this.itemsAvailable = itemsAvailable;
        this.soldItemsList = new HashMap<>();
        this.registers = new HashSet<>();

        this.overchargeByCategory = new EnumMap<>(ItemCategory.class);
    }

    public Store( String name, int daysTillExpirationAllowed, int percentageSale,  Map<Item, Double> soldItemsList ) {
        this.name = name;
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.soldItemsList = soldItemsList;
        this.soldItemsList = new HashMap<>();
        this.registers = new HashSet<>();

        this.overchargeByCategory = new EnumMap<>(ItemCategory.class);
    }



    // Overcharge
    public void setOverchargeByCategory(ItemCategory category, BigDecimal overcharge){
        overchargeByCategory.put( category, overcharge);
    }
    public BigDecimal getOverchargeByCategory(ItemCategory category) {
        return overchargeByCategory.get(category);
    }


    // Assign a cashier to a register
    @Override
    public boolean assignCashier(Cashier cashier,  Register register) throws RegisterUnavailableException, CashierUnavailableException {
        if (registers.contains(register)){
            throw new RegisterUnavailableException("The register you have chosen is unavailable");
        }
        if ( register.getCashier().equals(cashier) ){
            throw new CashierUnavailableException("The cashier you have chosen is unavailable");
        }
        if ( !register.getCashier().equals(cashier) ){
            register.setCashier(cashier);
            cashier.setRegister(register);
        }
        return true;
    }

     // 1 . Calculate the sum of all the items sold
     @Override
     public BigDecimal calculateItemsSoldRevenue(){
        BigDecimal revenue = BigDecimal.valueOf(0);

        for (Map.Entry<Item, Double> entry: soldItemsList.entrySet()) {
            BigDecimal itemPrice = entry.getKey().calculateFinalSellingPrice();
            BigDecimal itemUnites = BigDecimal.valueOf( entry.getValue() );

            //   revenue += entry.getKey().calculateFinalSellingPrice() * entry.getValue();
            revenue = revenue.add( itemPrice.multiply( itemUnites ) ) ;
        }
        return revenue;
    }
    // 2. Calculate the costs for the salaries of the employees
    @Override
    public BigDecimal calculateCashiersSalaries(){
         BigDecimal salaries = BigDecimal.valueOf(0);

         for ( Cashier currentCashier: cashiersList ) {
             // salaries += currentCashier.getMonthlySalary();

             salaries = salaries. add( currentCashier.getMonthlySalary() );
         }
         return salaries;
     }


    // 3. Calculate the sum the delivery will cost
    @Override
    public BigDecimal calculateDeliveryCosts(){
         BigDecimal costs = BigDecimal.valueOf(0);

         for (Map.Entry<Item, Double> entry: soldItemsList.entrySet()) {
             BigDecimal deliveryPrice =  entry.getKey().getDeliveryPrice();
             BigDecimal itemUnites = BigDecimal.valueOf(  entry.getValue()  );

             //   costs += entry.getKey().getDeliveryPrice() * entry.getValue();
             costs = costs.add( deliveryPrice.multiply( itemUnites )  );
         }
         return costs;
     }

     // 4. Calculate the total revenue




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

    public String getName() {
        return name;
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
