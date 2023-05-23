import Store.Item;
import Store.People.Client;
import Store.Register;
import Store.Receipt;


import Store.People.Cashier;
import Store.Store;

import Store.enums.ItemCategory;
import exeptions.*;
import exeptions.moneyExceptions.IncorrectClientBudgetException;
import exeptions.moneyExceptions.IncorrectPriceValueException;
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

    Map<Item, BigDecimal> items; Map<Item, BigDecimal> cart; Item item1, item2, item3, item4;
    BigDecimal itemUnites = BigDecimal.valueOf(2); BigDecimal priceDelivery = BigDecimal.valueOf(100);

    Client client;
    BigDecimal clientBudget = BigDecimal.valueOf(100);

    @BeforeEach
    void setUp() throws IncorrectPriceValueException {
        store = new Store("Lidl", daysTillExpirationAllowed, salePercentage  );

        // items
        item1 = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10));
        item2 = new Item("A2", "Jam",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10));
        item3 = new Item("A3", "Bread",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10));
        items = new HashMap<>( Map.of(item1, itemUnites, item2, itemUnites, item3, itemUnites) );
        cart = new HashMap<>( items );


        //cashier
        cashier = new Cashier("Bob", "C1", cashierSalary);
        cashiers = new HashSet<>(List.of(cashier));


        //client
        client = new Client(clientBudget, cart);
        Queue<Client> clients = new ArrayDeque<>(List.of(client));

        //register
        register = new Register(cashier, clients, store);
        registers = new HashSet<>(List.of(register));


        store0 = new Store("Lidl", daysTillExpirationAllowed, salePercentage, cashiers , items, items, registers );
        store0.setOverchargeByCategory(ItemCategory.CONSUMABLE, BigDecimal.valueOf(10));

        for (Item item : store0.getItemsAvailable().keySet()) {
            item.setStore(store0);
        }
        for (Register register:store0.getRegisters()) {
            register.setStore(store0);
        }
        item4 = new Item("A4", "Banana",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().minusDays(1), store0);
    }

    @Test
    void addClient() {
        client = new Client(BigDecimal.valueOf(300), items);
        assertTrue(register.addClient(client));
    }
    @Test
    void addClientAlreadyAdded() {
        assertFalse(register.addClient(client));
    }

    @Test
    void removeClient() {
        assertTrue(register.removeClient(client));
    }

    @Test
    void removeClientNotInQueue() {
        client = new Client(BigDecimal.valueOf(300), cart);
        assertFalse(register.removeClient(client));
    }

    @Test
    void checkForAvailabilityYes()  {
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
        assertNull(register.removeExpiredItem(item4, client));
    }

    @Test
    void scanItems() throws ItemHasExpiredException, ItemAmountInsufficientException {
        BigDecimal expected = BigDecimal.valueOf( 6*110 );
        assertEquals(expected, register.scanItems(client));
    }

    @Test
    void scanItemsExpiredItemFound()  {
        items = new HashMap<>( Map.of(item1, itemUnites, item2, itemUnites, item4, itemUnites) );
        client.setItems(items);
        assertThrows(ItemHasExpiredException.class, ()->register.scanItems(client));
    }

    @Test
    void scanItemsItemInsufficientAmount()  {
        client.getItems().put(item1, itemUnites.add(BigDecimal.valueOf(1)));
        assertThrows(ItemAmountInsufficientException.class, ()->register.scanItems(client));
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
        Receipt receipt = new Receipt(cashier);
        register.setReceipts( new HashSet<>(List.of(receipt)) );
        assertFalse(register.addReceipt(receipt));
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
    void removeSoldItemsFromAvailableStoreInventoryEmpty()  {
        register.setStore(store);
        assertThrows(NoItemsAvailableException.class, ()->register.removeSoldItemsFromAvailable(client));
    }

    @Test
    void removeSoldItemsFromAvailableClientListEmpty()  {
        client = new Client(clientBudget, new HashMap<>());
        assertThrows(NoItemsAvailableException.class, ()->register.removeSoldItemsFromAvailable(client));
    }

    @Test
    void finalizeTransactionPass() throws IncorrectClientBudgetException, NoItemsAvailableException {
        register.getCashier().setRegister(register);
        assertTrue(register.finalizeTransaction(client, clientBudget .subtract(BigDecimal.valueOf(1)) ));
    }

    @Test
    void finalizeTransactionNoPass() throws IncorrectClientBudgetException, NoItemsAvailableException {
        register.getCashier().setRegister(register);
        assertFalse(register.finalizeTransaction(client, clientBudget.add(BigDecimal.valueOf(1) ) ));
    }

}