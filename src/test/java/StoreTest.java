import Store.Item;
import Store.Register;

import Store.People.Cashier;
import Store.Store;
import Store.Receipt;

import Store.enums.ItemCategory;
import exeptions.CashierUnavailableException;
import exeptions.IncorrectPriceValueException;
import exeptions.RegisterUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    Store store;
    int daysTillExpirationAllowed = 5;
    int salePercentage = 20;

    HashSet<Cashier> cashiers ;
    Cashier cashier1;
    Cashier cashier4;
    BigDecimal cashierSalary = BigDecimal.valueOf(1500);

    Receipt receipt1;

    Register register1;
    Register register4;
    HashSet<Register> registers;

    Map<Item, BigDecimal> items;
    Item item1;
    BigDecimal itemUnites = BigDecimal.valueOf(2);
    BigDecimal priceDelivery = BigDecimal.valueOf(100);


    @BeforeEach
    void setUp() throws IncorrectPriceValueException {
        store = new Store("Lidl", daysTillExpirationAllowed, salePercentage  );
        store.setOverchargeByCategory(ItemCategory.CONSUMABLE, BigDecimal.valueOf(10));

        // items
        item1 = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);
        Item item2 = new Item("A2", "Jam",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);
        Item item3 = new Item("A3", "Bread",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);
        items = new HashMap<Item, BigDecimal>( Map.of(item1, itemUnites, item2, itemUnites, item3, itemUnites) );

        //cashiers
        cashier1 = new Cashier("Bob", "C1", cashierSalary);
        Cashier cashier2 = new Cashier("Bob", "C2", cashierSalary);
        Cashier cashier3 = new Cashier("Bob", "C3", cashierSalary);
        cashiers = new HashSet<>(Arrays.asList(cashier1, cashier2, cashier3));

        //receipts
        receipt1 = new Receipt(cashier1);
        Receipt receipt2 = new Receipt(cashier2);
        HashSet<Receipt> receipts = new HashSet<>(Arrays.asList(receipt1, receipt2) );

        //registers
        register1 = new Register(cashier1, store, receipts);
        Register register2 = new Register(cashier1, store);
        Register register3 = new Register(cashier1, store);
        registers = new HashSet<>(Arrays.asList(register1, register2, register3));

        store = new Store("Lidl", daysTillExpirationAllowed, salePercentage, cashiers , items, items, registers );
    }

    @Test
    void assignCashier() throws CashierUnavailableException, RegisterUnavailableException {
        assertTrue(store.assignCashier( cashier1, register1 ));
    }
    @Test
    void assignCashierRegisterUnavailable() throws CashierUnavailableException, RegisterUnavailableException {
        assertThrows(CashierUnavailableException.class, ()-> store.assignCashier( cashier4, register1 ));
    }

    @Test
    void assignCashierUnavailable() {
        assertThrows(RegisterUnavailableException.class, ()-> store.assignCashier( cashier1, register4 ));
    }


    @Test
    void calculateItemsSoldRevenueMultipleInput() {
        BigDecimal expected = BigDecimal.valueOf( 110*6 );
        assertEquals(expected, store.calculateItemsSoldRevenue());
    }

    @Test
    void calculateItemsSoldRevenueEmptyInput() {
        store = new Store("Lidl", daysTillExpirationAllowed, 20 );

        BigDecimal expected = BigDecimal.valueOf(0);
        assertEquals(expected, store.calculateItemsSoldRevenue());
    }

    @Test
    void calculateItemsSoldRevenueSingleInput() {
        items = new HashMap<>();
        items.put(item1, BigDecimal.valueOf(2));
        store = new Store("Lidl", daysTillExpirationAllowed, 20, items );

        BigDecimal expected = BigDecimal.valueOf(2*110);
        assertEquals(expected, store.calculateItemsSoldRevenue());
    }


    @Test
    void calculateCashiersSalaries() {
        BigDecimal expected = BigDecimal.valueOf(3 * 1500);
        assertEquals(expected, store.calculateCashiersSalaries());
    }

    @Test
    void calculateCashiersSalariesEmptyInput() {
        store = new Store("Lidl", daysTillExpirationAllowed, 20 );

        BigDecimal expected = BigDecimal.valueOf(0);
        assertEquals(expected, store.calculateCashiersSalaries());
    }

    @Test
    void calculateCashiersSalariesSingleInput() {
        cashiers = new HashSet<>();
        cashiers.add(cashier1);
        store = new Store("Lidl", daysTillExpirationAllowed, 20, cashiers, items  );

        BigDecimal expected = BigDecimal.valueOf(1500);
        assertEquals(expected, store.calculateCashiersSalaries());
    }

    @Test
    void calculateDeliveryCosts() {
        BigDecimal expected = BigDecimal.valueOf( 6*100 );
        assertEquals(expected, store.calculateDeliveryCosts());
    }


    @Test
    void calculateDeliveryCostsEmptyInput() {
        store = new Store("Lidl", daysTillExpirationAllowed, 20 );

        BigDecimal expected = BigDecimal.valueOf( 0 );
        assertEquals(expected, store.calculateDeliveryCosts());
    }

    @Test
    void calculateDeliveryCostsSingleInput() {
        BigDecimal expected = BigDecimal.valueOf( 6*100 );
        assertEquals(expected, store.calculateDeliveryCosts());
    }



    @Test
    void calculateTotalRevenuePositive() {
        cashier1 = new Cashier("Bob", "C1", BigDecimal.valueOf(1));
        cashiers = new HashSet<>();
        cashiers.add(cashier1);
        store = new Store("Lidl", daysTillExpirationAllowed, 20, cashiers, items, items, registers  );


        BigDecimal expected = BigDecimal.valueOf( 6*110 - (6*100 + 1) );
        assertEquals(expected, store.calculateTotalRevenue());
    }

    @Test
    void calculateTotalRevenueNegative() {
        BigDecimal expected = BigDecimal.valueOf( 6*110 - (6*100 + 3*1500) );
        assertEquals(expected, store.calculateTotalRevenue());
    }

    @Test
    void calculateTotalRevenueEmptyInput() {
        store = new Store("Lidl", daysTillExpirationAllowed, 20 );

        BigDecimal expected = BigDecimal.valueOf( 0 );
        assertEquals(expected, store.calculateTotalRevenue());
    }

    @Test
    void getNumberOfGivenReceipts() {
        int expected = 2;
        assertEquals(expected, store.getNumberOfGivenReceipts());
    }

    @Test
    void getNumberOfGivenReceiptsZero() {
        store = new Store("Lidl", daysTillExpirationAllowed, 20 );

        int expected = 0;
        assertEquals(expected, store.getNumberOfGivenReceipts());
    }
}