package Store;

import Store.enums.ItemCategory;
import exeptions.ItemHasExpiredException;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

public class Item {
    private String idNumber;
    private String name;
    private double deliveryPrice;
    private ItemCategory category;
    private LocalDate expirationDate;

    private Store store;

    public Item(String idNumber, String name, double deliveryPrice, ItemCategory category, LocalDate expirationDate) {
        this.idNumber = idNumber;
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.category = category;
        this.expirationDate = expirationDate;
    }

    // 1. See how many days there are left till expiration of product
    public long getDaysTillExpiration(){
        Duration duration = Duration.between( LocalDate.now().atStartOfDay(), expirationDate.atStartOfDay() );
        long remainingDays = duration.toDays();
        return remainingDays;
    }


    // 2. Calculate the price the item depending on whether it's consumable or not
    public double calculatePrice() {
        return deliveryPrice + ( deliveryPrice * category.getPercentageMarkup() ) / 100;
    }

    // 3. Calculate the price the item will sell for (adjust if the expiration is near)
    public double calculateFinalSellingPrice(){
        double sellingPrice = calculatePrice();

        if (getDaysTillExpiration() < store.getDaysTillExpirationAllowed()){
            return sellingPrice - ( sellingPrice * store.getPercentageSale()) / 100;
        }
        return sellingPrice;
    }

    // 4. Check if it has already expired
    public boolean hasExpired(){
        if(getDaysTillExpiration() < 0) {
            return true;
        }
        return false;
    }

    // 5. Sell if it has not, do not sell if it has
    public boolean sellItem()throws ItemHasExpiredException {
        if(hasExpired()){
            throw new ItemHasExpiredException("Item cannot be sold because it has expired");
        }
        return true;
    }

    public boolean putInAvailable(double units) throws ItemHasExpiredException {
        if (sellItem()){
            if (store.getItemsAvailable().containsKey(this)){
                units += store.getItemsAvailable().get(this);
            }
            store.getItemsAvailable().put(this,  units);


            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Item{" +
                "idNumber='" + idNumber + '\'' +
                ", name='" + name + '\'' +
                ", deliveryPrice=" + deliveryPrice +
                ", category=" + category +
                ", expirationDate=" + expirationDate +
                ", store=" + store +
                '}';
    }

    public String itemInfoReceipt() {
        return "" + name + "\t\t\t" +
                 calculateFinalSellingPrice() +
                ", expirationDate=" + expirationDate +
                ", store=" + store +
                '}';
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getName() {
        return name;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public Store getStore() {
        return store;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(idNumber, item.idNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumber);
    }
}
