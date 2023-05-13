import Store.Item;
import Store.Register;

import Store.People.Cashier;
import Store.Store;
import Store.enums.ItemCategory;
import exeptions.CashierUnavailableException;
import exeptions.IncorrectPriceValueException;
import exeptions.RegisterUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    Store store;
    int daysTillExpirationAllowed = 5;
    HashSet<Cashier> cashiers ;
    Cashier cashier1;
    Cashier cashier4;

    Register register1;
    Register register4;

    Map<Item, BigDecimal> items;
    Item item1;

    @BeforeEach
    void setUp() throws IncorrectPriceValueException {
        //ItemCategory.CONSUMABLE.setPercentageMarkup(10);
        store = new Store("Lidl", daysTillExpirationAllowed, 20  );
        store.setOverchargeByCategory(ItemCategory.CONSUMABLE, BigDecimal.valueOf(10));

        item1 = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, BigDecimal.valueOf(100), LocalDate.now().plusDays(10), store);
        Item item2 = new Item("A2", "Jam",  ItemCategory.CONSUMABLE, BigDecimal.valueOf(100), LocalDate.now().plusDays(10), store);
        Item item3 = new Item("A3", "Bread",  ItemCategory.CONSUMABLE, BigDecimal.valueOf(100), LocalDate.now().plusDays(10), store);

        items = new HashMap<Item, BigDecimal>();
        items.put(item1, BigDecimal.valueOf(2));
        items.put(item2, BigDecimal.valueOf(2));
        items.put(item3, BigDecimal.valueOf(2));

        cashier1 = new Cashier("Bob", "C1", BigDecimal.valueOf(1500));
        Cashier cashier2 = new Cashier("Bob", "C2", BigDecimal.valueOf(1500));
        Cashier cashier3 = new Cashier("Bob", "C3", BigDecimal.valueOf(1500));

        cashiers = new HashSet<>();
        cashiers.add(cashier1);
        cashiers.add(cashier2);
        cashiers.add(cashier3);

        register1 = new Register(cashier1, store);
        Register register2 = new Register(cashier1, store);
        Register register3 = new Register(cashier1, store);
        HashSet<Register> registers = new HashSet<>();
        registers.add(register1);
        registers.add(register2);
        registers.add(register3);

        store = new Store("Lidl", daysTillExpirationAllowed, 20, cashiers , items, items, registers );
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
    void calculateItemsSoldRevenueMultipleItems() {
        BigDecimal expected = BigDecimal.valueOf( 110*6 );
        assertEquals(expected, store.calculateItemsSoldRevenue());
    }
    //kolekciq s 1, 0 i mnogo elementi
    @Test
    void calculateItemsSoldRevenueZero() {
        items = new HashMap<Item, BigDecimal>();
        store = new Store("Lidl", daysTillExpirationAllowed, 20, cashiers, items  );

        BigDecimal expected = BigDecimal.valueOf(0);
        assertEquals(expected, store.calculateItemsSoldRevenue());
    }

    @Test
    void calculateItemsSoldRevenueSingleItem() {
        items =new HashMap<>();
        items.put(item1, BigDecimal.valueOf(2));
        store = new Store("Lidl", daysTillExpirationAllowed, 20, cashiers, items  );

        BigDecimal expected = BigDecimal.valueOf(110);
        assertEquals(expected, store.calculateItemsSoldRevenue());
    }


    @Test
    void calculateCashiersSalaries() {
        BigDecimal expected = BigDecimal.valueOf(3 * 1500);
        assertEquals(expected, store.calculateCashiersSalaries());
    }

    @Test
    void calculateDeliveryCosts() {
        BigDecimal expected = BigDecimal.valueOf( 6*100 );
        assertEquals(expected, store.calculateDeliveryCosts());
    }

    @Test
    void calculateTotalRevenuePositive() {
        BigDecimal expected = BigDecimal.valueOf( 6*110 - (6*100 + 3*1500) );
        assertEquals(expected, store.calculateTotalRevenue());
    }

    @Test
    void calculateTotalRevenueNegative() {
        BigDecimal expected = BigDecimal.valueOf( 6*110 - (6*100 + 3*1500) );
        assertEquals(expected, store.calculateTotalRevenue());
    }

    @Test
    void calculateTotalRevenueZero() {
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
        int expected = 0;
        assertEquals(expected, store.getNumberOfGivenReceipts());
    }
}