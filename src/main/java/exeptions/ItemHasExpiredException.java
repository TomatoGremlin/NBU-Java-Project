package exeptions;

import Store.Item;

public class ItemHasExpiredException extends Exception {
    Item item;
    public ItemHasExpiredException(String message, Item item) {
        super(message);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
