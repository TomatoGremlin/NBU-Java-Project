package Store;

import Store.Interfaces.DeliveryServices;
import Store.Interfaces.ItemPriceServices;
import Store.enums.ItemCategory;
import exeptions.IncorrectPriceValueException;
import exeptions.ItemHasExpiredException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

public class Item implements DeliveryServices, ItemPriceServices {
    private final String idNumber;
    private String name;
    private BigDecimal deliveryPrice;
    private ItemCategory category;
    private final LocalDate expirationDate;

    private Store store;

    public Item(String idNumber, String name,  ItemCategory category, BigDecimal deliveryPrice, LocalDate expirationDate, Store store) throws IncorrectPriceValueException {

        this.idNumber = idNumber;
        this.name = name;
        setDeliveryPrice( deliveryPrice );
        this.category = category;
        this.expirationDate = expirationDate;
        this.store = store;
    }
    public Item(String idNumber, String name,  ItemCategory category, BigDecimal deliveryPrice, LocalDate expirationDate) throws IncorrectPriceValueException {

        this.idNumber = idNumber;
        this.name = name;
        setDeliveryPrice( deliveryPrice );
        this.category = category;
        this.expirationDate = expirationDate;
        this.store = null;
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) throws IncorrectPriceValueException {
        //deliveryPrice <= 0
        if ( deliveryPrice.compareTo( BigDecimal.valueOf(0) ) <= 0 ){
            throw new IncorrectPriceValueException("The delivery price of an item should not be negative or 0", deliveryPrice);
        }
        this.deliveryPrice = deliveryPrice;
    }

    // 1. See how many days there are left till expiration of product
    public long getDaysTillExpiration(){
        return Duration.between( LocalDate.now().atStartOfDay(), expirationDate.atStartOfDay() ) . toDays();
    }


    // 2. Calculate the price the item depending on whether it's consumable or not
    @Override
    public BigDecimal calculatePrice() {
        //  deliveryPrice + ( deliveryPrice * itemOvercharge ) / 100 ;
        BigDecimal itemOvercharge = store.getOverchargeByCategory(category);
        return deliveryPrice.add ( (deliveryPrice.multiply(itemOvercharge) ). divide( BigDecimal.valueOf(100) ) );
    }

    // 3. Calculate the price the item will sell for (adjust if the expiration is near)
    @Override
    public BigDecimal calculateFinalSellingPrice(){
        BigDecimal sellingPrice = calculatePrice();

        if (getDaysTillExpiration() < store.getDaysTillExpirationAllowed()){
            BigDecimal sale = BigDecimal.valueOf( store.getPercentageSale() ) ;

            //   sellingPrice - ( sellingPrice * sale ) / 100;
            return sellingPrice.subtract ( sellingPrice.multiply(sale)  ). divide( BigDecimal.valueOf(100) ) ;
        }
        return sellingPrice;
    }

    // 4. Check if it has already expired
    @Override
    public boolean hasExpired(){
        return getDaysTillExpiration() < 0;
    }

    // 5. Sell if it has not, do not sell if it has
    @Override
    public boolean isSellable() {
        return !hasExpired();
    }

    //6. Put in store inventory

    @Override
    public boolean putInAvailable(BigDecimal units) throws ItemHasExpiredException {
        if (!isSellable()){
            throw new ItemHasExpiredException("Item cannot be sold because it has expired", this);
        }

        if (store.getItemsAvailable().containsKey(this)){
            units = units.add( store.getItemsAvailable().get(this)  );
        }
        store.getItemsAvailable().put(this,  units);
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

    public BigDecimal getDeliveryPrice() {
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

    public void setStore(Store store) {
        this.store = store;
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
