package Store;

import java.time.Duration;
import java.time.LocalDate;

public class Item {
    private String idNumber;
    public String name;
    private double deliveryPrice;
    private itemCategory category;
    private LocalDate expirationDate;

    private Store store;

    public Item(String idNumber, String name, double deliveryPrice, itemCategory category, LocalDate expirationDate) {
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
        if (remainingDays < store.daysTillExpirationAllowed){
            return sellingPrice - ( sellingPrice * store.percentageSale ) / 100;
        }
        else{
            return sellingPrice;
        }

    }





}
