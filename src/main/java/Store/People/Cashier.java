package Store.People;

import Store.Register;

import java.util.Objects;

public class Cashier {
    private String name;
    private String idNumber;
    private double monthlySalary;
    private Register register;


    public Cashier(String name, String idNumber, double monthlySalary) {
        this.name = name;
        this.idNumber = idNumber;
        this.monthlySalary = monthlySalary;
    }
    public Cashier(String name, String idNumber, double monthlySalary, Register register) {
        this.name = name;
        this.idNumber = idNumber;
        this.monthlySalary = monthlySalary;
        this.register = register;
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

    public Register getCheckout() {
        return register;
    }

    @Override
    public String toString() {
        return "Cashier{" +
                "name='" + name + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", monthlySalary=" + monthlySalary +
                ", checkout=" + register +
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

    public void setRegister(Register register) {
        this.register = register;
    }
}
