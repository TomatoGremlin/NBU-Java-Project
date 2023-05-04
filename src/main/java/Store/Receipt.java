package Store;

import Store.People.Cashier;
import exeptions.ItemAmountUnavailableException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Receipt {
    private static int num_instances = 0;
    private int id_number;
    private Cashier cashier;
    private LocalDate date;
    private Map<Item, Integer> itemsBought ;



    public Receipt(Cashier cashier, LocalDateTime date, Map<Item, Integer> itemsBoughtList) {
        num_instances++;
        this.id_number = num_instances;
        this.cashier = cashier;
        this.date = LocalDate.from(date);
        this.itemsBoughtList = itemsBoughtList;
    }

    public double sumItems() throws ItemAmountUnavailableException {
        double sumOwed = 0;
        for (Item currentItem: items ) {

            if (checkForAvailability(currentItem, itemAmount )){
                sumOwed += currentItem.calculateSellingPrice();
            }
            else{
                throw new ItemAmountUnavailableException( item.getName() + " only has "+ unitsAvailable +
                        " when " + itemAmount + " are needed", unitsAvailable);
            }
        }
        return sumOwed;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id_number=" + id_number +
                ", cashier=" + cashier +
                ", date=" + date +
                ", itemsBoughtList=" + itemsBoughtList +
                ", sum=" + sum +
                '}';
    }
}
