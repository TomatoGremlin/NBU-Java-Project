package Store.Interfaces;

import exeptions.ItemHasExpiredException;

import java.math.BigDecimal;

public interface DeliveryServices {
    public boolean hasExpired();
    public boolean isSellable()throws ItemHasExpiredException;
    public boolean putInAvailable(BigDecimal units) throws ItemHasExpiredException;

}
