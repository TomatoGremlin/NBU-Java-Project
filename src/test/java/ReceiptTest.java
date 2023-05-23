import Store.Receipt;
import Store.Item;
import Store.Store;
import Store.enums.ItemCategory;
import exeptions.moneyExceptions.IncorrectPriceValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {
    Receipt receipt;
    @BeforeEach
    void setUp() throws IncorrectPriceValueException {
        BigDecimal itemUnites = BigDecimal.valueOf(2); BigDecimal priceDelivery = BigDecimal.valueOf(100);
        int daysTillExpirationAllowed = 5; int salePercentage = 20;

        Store store = new Store("Lidl", daysTillExpirationAllowed, salePercentage);
        store.setOverchargeByCategory(ItemCategory.CONSUMABLE, BigDecimal.valueOf(10));

        Item item1 = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);
        Item item2 = new Item("A2", "Jam",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);
        Item item3 = new Item("A3", "Bread",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);
        Map<Item, BigDecimal> items = new HashMap<>( Map.of(item1, itemUnites, item2, itemUnites, item3, itemUnites) );

        receipt = new Receipt(items);

    }

    @Test
    void getTotalSumOfItems() {
        BigDecimal expected = BigDecimal.valueOf(660);
        assertEquals(expected, receipt.getTotalSumOfItems());
    }


    @Test
    void getTotalSumOfItemsZero() {
        receipt = new Receipt();
        BigDecimal expected = BigDecimal.valueOf(0);
        assertEquals(expected, receipt.getTotalSumOfItems());
    }
}