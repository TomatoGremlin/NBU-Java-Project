package Store.Interfaces;

import Store.Receipt;

public interface ReceiptServices {
    public boolean addReceipt(Receipt receipt);
    public void showReceipt( Receipt receipt );
}
