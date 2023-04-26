package Store.People;

import Store.Checkout;

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
}