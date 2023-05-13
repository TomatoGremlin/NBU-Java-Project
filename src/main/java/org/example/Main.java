package org.example;

import Store.People.Cashier;
import Store.Store;
import Store.Register;
import Store.Item;

import Store.enums.ItemCategory;
import exeptions.IncorrectPriceValueException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IncorrectPriceValueException {

        Store store;
        int daysTillExpirationAllowed = 5;
        HashSet<Cashier> cashiers ;
        Cashier cashier1;
        Cashier cashier4;

        Register register1;
        Register register4;

        Map<Item, BigDecimal> items;
        Item item1;


            //ItemCategory.CONSUMABLE.setPercentageMarkup(10);
            store = new Store("Lidl", daysTillExpirationAllowed, 20  );

            item1 = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, BigDecimal.valueOf(100), LocalDate.now().plusDays(10), store);
            Item item2 = new Item("A2", "Jam",  ItemCategory.CONSUMABLE, BigDecimal.valueOf(100), LocalDate.now().plusDays(10), store);
            Item item3 = new Item("A3", "Bread",  ItemCategory.CONSUMABLE, BigDecimal.valueOf(100), LocalDate.now().plusDays(10), store);

            items = new HashMap<Item, BigDecimal>();
            items.put(item1, 2.0);
            items.put(item2, 2.0);
            items.put(item3, 2.0);

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
            store.setOverchargeByCategory(ItemCategory.CONSUMABLE, BigDecimal.valueOf(10));
        BigDecimal ok =     store.getOverchargeByCategory(item3.getCategory());
        System.out.println(item3.calculateFinalSellingPrice());


    }
}