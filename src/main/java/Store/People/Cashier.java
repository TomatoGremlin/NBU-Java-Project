package Store.People;

import Store.Register;
import exeptions.IncorrectSalaryValueException;

import java.math.BigDecimal;
import java.util.Objects;

public class Cashier {
    private String name;
    private String idNumber;
    private BigDecimal monthlySalary;
    private Register register;


    public Cashier(String name, String idNumber, BigDecimal monthlySalary) {
        this.name = name;
        this.idNumber = idNumber;
        this.monthlySalary = monthlySalary;
    }
    public Cashier(String name, String idNumber, BigDecimal monthlySalary, Register register) {
        this.name = name;
        this.idNumber = idNumber;
        this.monthlySalary = monthlySalary;
        this.register = register;
    }

    public void setMonthlySalary(BigDecimal monthlySalary) throws IncorrectSalaryValueException {
        // if salary <= 0
        if ( monthlySalary.compareTo(BigDecimal.valueOf(0)) != 1){
            throw new IncorrectSalaryValueException("Cashier's salary should not be a negative number or 0");
        }
        this.monthlySalary = monthlySalary;
    }

    public String getName() {
        return name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public BigDecimal getMonthlySalary() {
        return monthlySalary;
    }

    public Register getRegister() {
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
