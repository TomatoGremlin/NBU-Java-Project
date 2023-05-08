package Store;

import Store.People.Cashier;
import exeptions.ItemAmountUnavailableException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        this.itemsBought = itemsBoughtList;
    }

    public double getTotalSumOfItems() {
        double sum = 0;
        for (Map.Entry<Item, Integer> entry: itemsBought.entrySet()) {
            sum += entry.getKey().calculateSellingPrice() * entry.getValue();
        }
        return sum;
    }


    public static int getNum_instances() {
        return num_instances;
    }

    public int getId_number() {
        return id_number;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<Item, Integer> getItemsBought() {
        return itemsBought;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id_number=" + id_number +
                ", cashier=" + cashier +
                ", date=" + date +
                ", itemsBought=" + itemsBought +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return id_number == receipt.id_number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_number);
    }
}
