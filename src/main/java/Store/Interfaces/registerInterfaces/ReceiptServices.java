package Store.Interfaces.registerInterfaces;

import Store.Receipt;

public interface ReceiptServices {
    public boolean addReceipt(Receipt receipt);
    public void showReceipt( Receipt receipt );
}
