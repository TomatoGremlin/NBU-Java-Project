package Store.People;

import Store.Checkout;

import java.util.Objects;

public class Cashier {
    private String name;
    private String idNumber;
    private double monthlySalary;
    private Checkout checkout;

    public Cashier(String name, String idNumber, double monthlySalary, Checkout checkout) {
        this.name = name;
        this.idNumber = idNumber;
        this.monthlySalary = monthlySalary;
        this.checkout = checkout;
    }

    public String getName() {
        return name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public Checkout getCheckout() {
        return checkout;
    }

    @Override
    public String toString() {
        return "Cashier{" +
                "name='" + name + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", monthlySalary=" + monthlySalary +
                ", checkout=" + checkout +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cashier cashier = (Cashier) o;
        return Objects.equals(idNumber, cashier.idNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idNumber);
    }
}
