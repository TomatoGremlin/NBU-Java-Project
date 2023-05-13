package Store.Interfaces;

import exeptions.ItemHasExpiredException;

public interface DeliveryServices {
    public boolean hasExpired();
    public boolean isSellable()throws ItemHasExpiredException;
    public boolean putInAvailable(double units) throws ItemHasExpiredException;

}
