package org.example;

import Store.People.Cashier;
import Store.People.Client;
import Store.Store;
import Store.Register;
import Store.Item;

import Store.enums.ItemCategory;
import exeptions.*;
import exeptions.moneyExceptions.IncorrectClientBudgetException;
import exeptions.moneyExceptions.IncorrectPriceValueException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IncorrectPriceValueException {
        //cashiers
        Cashier cashier1 = new Cashier("Steve", "C1", BigDecimal.valueOf(2500));
        Cashier cashier2 = new Cashier("Carl", "C2", BigDecimal.valueOf(2000));
        HashSet <Cashier> cashiers = new HashSet<>(List.of(cashier1, cashier2));



        //store
        Store store = new Store("Whole Foods", 5, 20, cashiers );
        store.setOverchargeByCategory(ItemCategory.CONSUMABLE, BigDecimal.valueOf(10));

        //registers
        Register register1 = new Register(store);
        Register register2 = new Register(store);
        Register register3 = new Register(store);
        HashSet<Register> registers = new HashSet<>(List.of(register1,register2, register3));

        store.setRegisters(registers);

        //assign cashier
        try {
            store.assignCashier(cashier1, register1);
        } catch (RegisterUnavailableException | CashierUnavailableException e) {
            throw new RuntimeException(e);
        }


        // items in store
        BigDecimal priceDelivery = BigDecimal.valueOf(2);
        Item item1 = new Item("A1", "Butter",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);
        Item item2 = new Item("A2", "Jam",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);
        Item item3 = new Item("A3", "Bread",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);

        try {
            item1.putInAvailable(BigDecimal.valueOf(30));
            item2.putInAvailable(BigDecimal.valueOf(20));
            item3.putInAvailable(BigDecimal.valueOf(10));

        } catch (ItemHasExpiredException e) {
            throw new RuntimeException(e);
        }


        //clients
        HashMap<Item, BigDecimal> cart = new HashMap<>( Map.of(item1, BigDecimal.valueOf(5), item2, BigDecimal.valueOf(10)) );
        Client client1 = new Client(BigDecimal.valueOf( 100 ), cart);

        register1.addClient(client1);

        // Clients' Transactions
        BigDecimal owedSum;
        try {
            owedSum = register1.scanItems(client1);
        } catch (ItemAmountInsufficientException | ItemHasExpiredException e) {
            throw new RuntimeException(e);
        }

        try {
            register1.finalizeTransaction(client1, owedSum );
        } catch (IncorrectClientBudgetException | NoItemsAvailableException e) {
            throw new RuntimeException(e);
        }


    }
}