package Store;

import Store.Interfaces.DeliveryServices;
import Store.Interfaces.ItemPriceServices;
import Store.enums.ItemCategory;
import exeptions.ItemHasExpiredException;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

public class Item implements DeliveryServices, ItemPriceServices {
    private final String idNumber;
    private String name;
    private double deliveryPrice;
    private ItemCategory category;
    private LocalDate expirationDate;

    private Store store;

    public Item(String idNumber, String name,  ItemCategory category, double deliveryPrice, LocalDate expirationDate, Store store) {

        this.idNumber = idNumber;
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.category = category;
        this.expirationDate = expirationDate;
        this.store = store;
    }

    // 1. See how many days there are left till expiration of product
    public long getDaysTillExpiration(){
        Duration duration = Duration.between( LocalDate.now().atStartOfDay(), expirationDate.atStartOfDay() );
        long remainingDays = duration.toDays();

        return remainingDays;
    }


    // 2. Calculate the price the item depending on whether it's consumable or not
    @Override
    public double calculatePrice() {
        return deliveryPrice + ( deliveryPrice * store.getOverchargeByCategory().get(this.category) ) / 100;
    }

    // 3. Calculate the price the item will sell for (adjust if the expiration is near)
    @Override
    public double calculateFinalSellingPrice(){
        double sellingPrice = calculatePrice();

        if (getDaysTillExpiration() < store.getDaysTillExpirationAllowed()){
            return sellingPrice - ( sellingPrice * store.getPercentageSale()) / 100;
        }
        return sellingPrice;
    }

    // 4. Check if it has already expired
    @Override
    public boolean hasExpired(){
        if(getDaysTillExpiration() < 0) {
            return true;
        }
        return false;
    }

    // 5. Sell if it has not, do not sell if it has
    @Override
    public boolean isSellable()throws ItemHasExpiredException {
        if(hasExpired()){
            throw new ItemHasExpiredException("Item cannot be sold because it has expired");
        }
        return true;
    }

    //6. Put in store inventory

    @Override
    public boolean putInAvailable(double units) throws ItemHasExpiredException {

        if (isSellable()){
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
