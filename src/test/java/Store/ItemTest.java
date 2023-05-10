package Store;

import Store.enums.ItemCategory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    Item item;
    Store store;

    @BeforeEach
    void setUp() {
        ItemCategory.CONSUMABLE.setPercentageMarkup(10);
        store = new Store("Lidl", 5, 20 );
        item = new Item("A1", "Pickles Jar",  ItemCategory.CONSUMABLE, 1, LocalDate.of(2023,5,30), store);
    }

    @Test
    void getDaysTillExpiration() {
        long expected = 9;
        assertEquals( expected, item.getDaysTillExpiration() );
    }

    @Test
    void calculatePrice() {
    }

    @Test
    void calculateFinalSellingPrice() {
    }

    @Test
    void hasExpired() {
    }

    @Test
    void sellItem() {
    }

    @Test
    void putInAvailable() {
    }
}