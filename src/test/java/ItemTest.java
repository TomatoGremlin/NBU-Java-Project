import Store.Item;
import Store.Store;
import Store.enums.ItemCategory;
import exeptions.IncorrectPriceValue;
import exeptions.ItemHasExpiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    Item item;
    Store store;
    int daysTillExpirationAllowed = 5;

    @BeforeEach
    void setUp() {
        ItemCategory.CONSUMABLE.setPercentageMarkup(10);
        store = new Store("Lidl", daysTillExpirationAllowed, 20 );
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, 100, LocalDate.now().plusDays(10), store);
    }

    @Test
    void getDaysTillExpirationPositive() {
        long expected = 10;
        assertEquals( expected, item.getDaysTillExpiration() );
    }

    @Test
    void getDaysTillExpirationZero() {
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, 100, LocalDate.now(), store);
        long expected = 0;
        assertEquals( expected, item.getDaysTillExpiration() );
    }

    @Test
    void getDaysTillExpirationNegative() throws IncorrectPriceValue {
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, 100, LocalDate.now().minusDays(1), store);
        long expected = -1;
        assertEquals( expected, item.getDaysTillExpiration() );
    }

    @Test
    void calculatePrice() {
        double expected = 110;
        assertEquals(expected, item.calculatePrice());
    }



    @Test
    void calculateFinalSellingPriceNoSale() {
        double expected = 110;
        assertEquals( expected, item.calculateFinalSellingPrice() );
    }


    @Test
    void calculateFinalSellingPriceWithSale() {
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, 100, LocalDate.now().minusDays(daysTillExpirationAllowed + 1), store);

        double expected = 88;
        assertEquals( expected, item.calculateFinalSellingPrice() );
    }

    @Test
    void hasExpiredNo() {
        assertFalse(item.hasExpired());
    }

    @Test
    void hasExpiredYes() {
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, 100, LocalDate.now().minusDays(1), store);
        assertTrue(item.hasExpired());
    }

    @Test
    void isSellableYes() throws ItemHasExpiredException {
        assertTrue(item.isSellable());
    }

    @Test
    void isSellableNo() throws ItemHasExpiredException {
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, 100, LocalDate.now().minusDays(1), store);

        assertThrows(ItemHasExpiredException.class, ()->item.isSellable());
    }

    @Test
    void putInAvailable() throws ItemHasExpiredException {
        assertTrue(item.putInAvailable(7));
    }

    @Test
    void putInAvailableExpired() throws ItemHasExpiredException {
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, 100, LocalDate.now().minusDays(1), store);

        assertThrows(ItemHasExpiredException.class, ()->item.putInAvailable(7));
    }
}