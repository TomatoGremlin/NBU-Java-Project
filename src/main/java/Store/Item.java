package Store;

import Store.enums.ItemCategory;
import exeptions.ItemHasExpiredException;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

public class Item {
    // items should be in map
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

    public long getDaysTillExpiration(){
        Duration duration = Duration.between( LocalDate.now().atStartOfDay(), expirationDate.atStartOfDay() );
        long remainingDays = duration.toDays();
        return remainingDays;
    }
    public boolean hasExpired(){
        if(getDaysTillExpiration() < 0) {
            return true;
        }
        return false;
    }
    public double calculateSellingPrice(){
        double sellingPrice = deliveryPrice + ( deliveryPrice * category.getPercentageMarkup() ) / 100;

        if (getDaysTillExpiration() < store.getDaysTillExpirationAllowed()){
            return sellingPrice - ( sellingPrice * store.getPercentageSale()) / 100;
        }
        return sellingPrice;
    }
    public boolean sellItem()throws ItemHasExpiredException {
        if(hasExpired()){
            throw new ItemHasExpiredException("Item cannot be sold because it has expired");
        }
        return true;
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
