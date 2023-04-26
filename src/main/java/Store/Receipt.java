package Store;

import Store.People.Cashier;

import java.time.LocalDate;

public class Receipt {
    public static int num_instances = 0;
    public int id_number;
    public Cashier cashier;
    public LocalDate date;
    public Checkout checkout;

    public Receipt( Cashier cashier, LocalDate date, Checkout checkout) {
        num_instances++;
        this.id_number = num_instances;

        this.cashier = cashier;
        this.date = date;
        this.checkout = checkout;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id_number=" + id_number +
                ", cashier=" + cashier +
                ", date=" + date +
                ", checkout=" + checkout +
                '}';
    }
}
