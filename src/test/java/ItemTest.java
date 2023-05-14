import Store.Item;
import Store.Store;
import Store.enums.ItemCategory;
import exeptions.IncorrectPriceValueException;
import exeptions.ItemHasExpiredException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    Item item;
    BigDecimal priceDelivery = BigDecimal.valueOf(100);

    Store store;
    int daysTillExpirationAllowed = 5;
    int salePercentage = 20;

    @BeforeEach
    void setUp() throws IncorrectPriceValueException {
        store = new Store("Lidl", daysTillExpirationAllowed, salePercentage );
        store.setOverchargeByCategory(ItemCategory.CONSUMABLE, BigDecimal.valueOf(10));

        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, priceDelivery, LocalDate.now().plusDays(10), store);
    }

    @Test
    void getDaysTillExpirationPositive() {
        long expected = 10;
        assertEquals( expected, item.getDaysTillExpiration() );
    }

    @Test
    void getDaysTillExpirationZero() throws IncorrectPriceValueException {
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, BigDecimal.valueOf(100), LocalDate.now(), store);
        long expected = 0;
        assertEquals( expected, item.getDaysTillExpiration() );
    }

    @Test
    void getDaysTillExpirationNegative() throws IncorrectPriceValueException {
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, BigDecimal.valueOf(100), LocalDate.now().minusDays(1), store);
        long expected = -1;
        assertEquals( expected, item.getDaysTillExpiration() );
    }

    @Test
    void calculatePrice() {
        BigDecimal expected = BigDecimal.valueOf( 110 );
        assertEquals(expected, item.calculatePrice());
    }



    @Test
    void calculateFinalSellingPriceNoSale() {
        BigDecimal expected = BigDecimal.valueOf( 110 );
        assertEquals( expected, item.calculateFinalSellingPrice() );
    }


    @Test
    void calculateFinalSellingPriceWithSale() throws IncorrectPriceValueException {
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, BigDecimal.valueOf(100), LocalDate.now().minusDays(daysTillExpirationAllowed + 1), store);

        BigDecimal expected = BigDecimal.valueOf( 88 );
        assertEquals( expected, item.calculateFinalSellingPrice() );
    }

    @Test
    void hasExpiredNo() {
        assertFalse(item.hasExpired());
    }

    @Test
    void hasExpiredYes() throws IncorrectPriceValueException {
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, BigDecimal.valueOf(100), LocalDate.now().minusDays(1), store);
        assertTrue(item.hasExpired());
    }

    @Test
    void isSellableYes()  {
        assertTrue(item.isSellable());
    }

    @Test
    void isSellableNo() throws IncorrectPriceValueException {
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, BigDecimal.valueOf(100), LocalDate.now().minusDays(1), store);

        assertFalse(item.isSellable());
    }

    @Test
    void putInAvailable() throws ItemHasExpiredException {
        assertTrue(item.putInAvailable(BigDecimal.valueOf(7)));
    }

    @Test
    void putInAvailableExpired() throws ItemHasExpiredException, IncorrectPriceValueException {
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, BigDecimal.valueOf(100), LocalDate.now().minusDays(1), store);

        assertThrows(ItemHasExpiredException.class, ()->item.putInAvailable(BigDecimal.valueOf( 7 )) );
    }
}