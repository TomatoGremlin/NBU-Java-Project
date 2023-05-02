package Store;

import Store.People.Cashier;

import java.time.LocalDate;
import java.util.List;

public class Receipt {
    private static int num_instances = 0;
    private int id_number;
    private Cashier cashier;
    private LocalDate date;
    private List<Item> itemsBoughtList;
    private double sum;


    public Receipt( Cashier cashier, LocalDate date, List<Item> itemsBoughtList, double sum) {
        num_instances++;
        this.id_number = num_instances;
        this.cashier = cashier;
        this.date = date;
        this.itemsBoughtList = itemsBoughtList;
        this.sum = sum;
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
