import Store.Item;
import Store.People.Client;
import Store.Register;
import Store.Receipt;


import Store.People.Cashier;
import Store.Store;

import Store.enums.ItemCategory;
import exeptions.IncorrectPriceValueException;
import exeptions.ItemAmountUnavailableException;
import exeptions.ItemHasExpiredException;
import exeptions.NoItemsAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RegisterTest {

    Store store, store0;
    int daysTillExpirationAllowed = 5; int salePercentage = 20;

    HashSet<Cashier> cashiers ; Cashier cashier;
    BigDecimal cashierSalary = BigDecimal.valueOf(1500);

    Register register; HashSet<Register> registers;

    Map<Item, BigDecimal> items; Item item1, item2, item3, item4;
    BigDecimal itemUnites = BigDecimal.valueOf(2); BigDecimal priceDelivery = BigDecimal.valueOf(100);

    Client client;
    BigDecimal clientBudget = BigDecimal.valueOf(100);

    @BeforeEach
    void setUp() throws IncorrectPriceValueException {
        store = new Store("Lidl", daysTillExpirationAllowed, salePercentage  );
        store.setOverchargeByCategory(ItemCategory.CONSUMABLE, BigDecimal.valueOf(10));

        // items
        item1 = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);
        item2 = new Item("A2", "Jam",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);
        item3 = new Item("A3", "Bread",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);
        items = new HashMap<Item, BigDecimal>( Map.of(item1, itemUnites, item2, itemUnites, item3, itemUnites) );

        //cashier
        cashier = new Cashier("Bob", "C1", cashierSalary);
        cashiers = new HashSet<>(Arrays.asList(cashier));


        //client
        client = new Client(clientBudget, items);
        Queue<Client> clients = new ArrayDeque<>(Arrays.asList(client));

        //register
        register = new Register(cashier, clients, store);
        registers = new HashSet<>(Arrays.asList(register));


        store0 = new Store("Lidl", daysTillExpirationAllowed, salePercentage, cashiers , items, items, registers );
        for (Item item : store0.getItemsAvailable().keySet()) {
            item.setStore(store0);
        }
        for (Register register:store0.getRegisters()) {
            register.setStore(store0);
        }
        item4 = new Item("A4", "Banana",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store0);
    }

    @Test
    void addClient() {
    }

    @Test
    void removeClient() {
    }

    @Test
    void checkForAvailabilityYes() throws IncorrectPriceValueException {
        assertTrue(register.checkForAvailability(item1, itemUnites));
    }

    @Test
    void checkForAvailabilityNo() {
        assertFalse(register.checkForAvailability(item1, itemUnites.add(BigDecimal.valueOf(1))));
    }

    @Test
    void removeExpiredItemSuccessful() {
        BigDecimal expected = itemUnites;
        assertEquals(expected, register.removeExpiredItem( item1, client) );
    }
    @Test
    void removeExpiredItemUnsuccessful() {
        assertEquals(null, register.removeExpiredItem( item4, client) );
    }

    @Test
    void scanItems() throws ItemHasExpiredException, ItemAmountUnavailableException {
        BigDecimal expected = BigDecimal.valueOf( 6*110 );
        assertEquals(expected, register.scanItems(client));
    }

    @Test
    void scanItemsExpiredItemFound() throws ItemHasExpiredException, ItemAmountUnavailableException {
        assertThrows(ItemHasExpiredException.class, ()->register.scanItems(client));
    }

    @Test
    void scanItemsItem() throws ItemHasExpiredException, ItemAmountUnavailableException {
        assertThrows(ItemAmountUnavailableException.class, ()->register.scanItems(client));
    }


    @Test
    void canTransactionPassYes() {
        assertTrue(register.canTransactionPass(client, clientBudget));
    }

    @Test
    void canTransactionPassNo() {
        assertFalse(register.canTransactionPass(client, clientBudget.add(BigDecimal.valueOf(1))));
    }

    @Test
    void addReceiptSuccessful() {
        Receipt receipt = new Receipt(cashier);
        assertTrue(register.addReceipt( receipt ));
    }

    @Test
    void addReceiptUnsuccessful() {
        //assertFalse(register.addReceipt());
    }

    @Test
    void addItemsToSoldNotEmpty() {
        Map<Item, BigDecimal> expected = Map.of(
                item1, itemUnites.add(itemUnites ),
                item2, itemUnites.add(itemUnites ),
                item3, itemUnites.add(itemUnites )
        );
        assertEquals(expected, register.addItemsToSold(client));
    }
    @Test
    void addItemsToSoldEmpty() {
        register.setStore(store);
        Map<Item, BigDecimal> expected = Map.of(
                item1, itemUnites,
                item2, itemUnites,
                item3, itemUnites
        );
        assertEquals(expected, register.addItemsToSold(client));
    }


    @Test
    void removeSoldItemsFromAvailable() throws NoItemsAvailableException {
        Map<Item, BigDecimal> expected = Map.of(
                item1, BigDecimal.valueOf(0),
                item2, BigDecimal.valueOf(0),
                item3, BigDecimal.valueOf(0)
        );
        assertEquals(expected, register.removeSoldItemsFromAvailable(client));
    }

    @Test
    void removeSoldItemsFromAvailableStoreInventoryEmpty() throws NoItemsAvailableException {
        register.setStore(store);
        assertThrows(NoItemsAvailableException.class, ()->register.removeSoldItemsFromAvailable(client));
    }

    @Test
    void removeSoldItemsFromAvailableClientListEmpty() throws NoItemsAvailableException {
        client = new Client(clientBudget, new HashMap<>());
        assertThrows(NoItemsAvailableException.class, ()->register.removeSoldItemsFromAvailable(client));
    }

    @Test
    void finalizeTransaction() {
    }
}