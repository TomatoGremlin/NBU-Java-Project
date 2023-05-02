package Store;

import Store.enums.ItemCategory;

import java.time.Duration;
import java.time.LocalDate;

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

    public double calculateSellingPrice(){
        Duration duration = Duration.between( LocalDate.now().atStartOfDay(), expirationDate.atStartOfDay() );
        long remainingDays = duration.toDays();

        double sellingPrice = deliveryPrice + ( deliveryPrice * category.getPercentageMarkup() ) / 100;
        if(remainingDays < 0){
            return 0;
        }
        if (remainingDays < store.getDaysTillExpirationAllowed()){
            return sellingPrice - ( sellingPrice * store.getPercentageSale()) / 100;
        }
        else{
            return sellingPrice;
        }

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
}
