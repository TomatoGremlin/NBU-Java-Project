package Store;

import Store.People.Cashier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

public class Receipt {
    private static int num_instances = 0;
    private final int id_number;
    private Cashier cashier;
    private LocalDateTime dateTime;
    private Map<Item, Double> itemsBought ;



    public Receipt( Cashier cashier, Map<Item, Double> itemsBoughtList ) {
        num_instances++;
        this.id_number = num_instances;
        this.cashier = cashier;
        this.dateTime = LocalDateTime.now();
        this.itemsBought = itemsBoughtList;
    }

    public BigDecimal getTotalSumOfItems() {
        BigDecimal sum = BigDecimal.valueOf(0);

        for (Map.Entry<Item, Double> entry: itemsBought.entrySet()) {
            BigDecimal price = entry.getKey().calculateFinalSellingPrice();
            BigDecimal unites = BigDecimal.valueOf( entry.getValue()  );

            //  sum += entry.getKey().calculateFinalSellingPrice() * entry.getValue();
            sum = sum.add( price.multiply( price.multiply( unites )  ) ) ;
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

    public LocalDateTime getDate() {
        return dateTime;
    }

    public Map<Item, Double> getItemsBought() {
        return itemsBought;
    }


    public String formatReceipt() {
        StringBuilder receiptBuilder = new StringBuilder();

        // Receipt header
        receiptBuilder.append(cashier.getRegister().getStore().getName()).append("\n");

        receiptBuilder.append("Customer Receipt Copy").append("\n");
        receiptBuilder.append("Receipt ID: ").append(id_number).append("\n");
        receiptBuilder.append("Cashier: ").append(cashier.getName()).append("\n");
        receiptBuilder.append("Date and Time: ").append(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");

        receiptBuilder.append("=======================================================");
        // Receipt items
        receiptBuilder.append("Items Bought:\n");
        for (Map.Entry<Item, Double> entry : itemsBought.entrySet()) {
            Item item = entry.getKey();
            double quantity = entry.getValue();
            receiptBuilder.append("- ").append(item.getName()).append("\t\t").append(quantity).append("\n");
            receiptBuilder.append( item.calculateFinalSellingPrice() ).append("\n");
        }

        receiptBuilder.append("=======================================================");

        // Receipt footer
        receiptBuilder.append("Total: ").append(getTotalSumOfItems()).append("\n");

        return receiptBuilder.toString();
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
