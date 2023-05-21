package Store;

import Store.Interfaces.storeInterfaces.CashierServices;
import Store.Interfaces.storeInterfaces.RevenueServices;
import Store.People.Cashier;
import Store.enums.ItemCategory;
import exeptions.CashierUnavailableException;
import exeptions.RegisterUnavailableException;

import java.math.BigDecimal;
import java.util.*;

public class Store implements RevenueServices, CashierServices {
    private String name;
    private int daysTillExpirationAllowed;
    private int percentageSale;
    private HashSet<Cashier> cashiersList; // hash set differentiating by id because otherwise it would be by referential
    private Map<Item, BigDecimal> itemsAvailable;
    private Map<Item, BigDecimal> soldItemsList;
    private HashSet<Register> registers;

    private EnumMap<ItemCategory, BigDecimal> overchargeByCategory;

    //input everything
    public Store(String name, int daysTillExpirationAllowed, int percentageSale, HashSet<Cashier> cashiersList, Map<Item, BigDecimal> itemsAvailable, Map<Item, BigDecimal> soldItemsList, HashSet<Register> registers) {
        this.name = name;
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.cashiersList = cashiersList;
        this.itemsAvailable = itemsAvailable;
        this.soldItemsList = soldItemsList;
        this.registers = registers;

        this.overchargeByCategory = new EnumMap<>(ItemCategory.class);
    }


    // input available items + cashiers
    public Store( String name , int daysTillExpirationAllowed, int percentageSale, HashSet<Cashier> cashiersList, Map<Item, BigDecimal> itemsAvailable ) {
        this.name = name;
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.cashiersList = cashiersList;
        this.itemsAvailable = itemsAvailable;
        this.soldItemsList = new HashMap<>();
        this.registers = new HashSet<>();

        this.overchargeByCategory = new EnumMap<>(ItemCategory.class);
    }

    //  cashiers
    public Store( String name , int daysTillExpirationAllowed, int percentageSale, HashSet<Cashier> cashiersList ) {
        this.name = name;
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.cashiersList = cashiersList;
        this.itemsAvailable = new HashMap<>();
        this.soldItemsList = new HashMap<>();
        this.registers = new HashSet<>();

        this.overchargeByCategory = new EnumMap<>(ItemCategory.class);
    }

    // input list of sold items
    public Store( String name, int daysTillExpirationAllowed, int percentageSale,  Map<Item, BigDecimal> soldItemsList ) {
        this.name = name;
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.soldItemsList = soldItemsList;
        this.itemsAvailable = new HashMap<>();

        this.registers = new HashSet<>();
        this.cashiersList = new HashSet<>();

        this.overchargeByCategory = new EnumMap<>(ItemCategory.class);
    }

    // store with empty collections
    public Store( String name, int daysTillExpirationAllowed, int percentageSale) {
        this.name = name;
        this.daysTillExpirationAllowed = daysTillExpirationAllowed;
        this.percentageSale = percentageSale;
        this.soldItemsList = new HashMap<>();
        this.itemsAvailable = new HashMap<>();

        this.registers = new HashSet<>();
        this.cashiersList = new HashSet<>();

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
    // if the cashier had been assigned to a different register before we should remove him from the old
    @Override
    public boolean assignCashier(Cashier cashier,  Register register) throws RegisterUnavailableException, CashierUnavailableException {
        if (!registers.contains(register)){
            throw new RegisterUnavailableException("The register you have chosen is unavailable", register);
        }
        if ( !cashiersList.contains(cashier) ){
            throw new CashierUnavailableException("The cashier you have chosen is unavailable", cashier);
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

        for (Map.Entry<Item, BigDecimal> entry: soldItemsList.entrySet()) {
            BigDecimal itemPrice = entry.getKey().calculateFinalSellingPrice();
            BigDecimal itemUnites =  entry.getValue() ;

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

         for (Map.Entry<Item, BigDecimal> entry: soldItemsList.entrySet()) {
             BigDecimal deliveryPrice =  entry.getKey().getDeliveryPrice();
             BigDecimal itemUnites =  entry.getValue() ;

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

    public Map<Item, BigDecimal> getItemsAvailable() {
        return itemsAvailable;
    }

    public Map<Item, BigDecimal> getSoldItemsList() {
        return soldItemsList;
    }

    public HashSet<Register> getRegisters() {
        return registers;
    }

    public void setRegisters(HashSet<Register> registers) {
        this.registers = registers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(name, store.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
