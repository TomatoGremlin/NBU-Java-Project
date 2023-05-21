package Store.Interfaces.storeInterfaces;

import java.math.BigDecimal;

public interface RevenueServices {
    // 1 . Calculate the sum of all the items sold
    public BigDecimal calculateItemsSoldRevenue();

    // 2. Calculate the costs for the salaries of the employees
    public BigDecimal calculateCashiersSalaries();

    // 3. Calculate the sum the delivery will cost
    public BigDecimal calculateDeliveryCosts();

    // 4. Calculate the total revenue
    public default BigDecimal calculateTotalRevenue(){
        //    calculateItemsSoldRevenue() - (calculateCashiersSalaries() + calculateDeliveryCosts()  );
        return calculateItemsSoldRevenue() .subtract (calculateCashiersSalaries() .add( calculateDeliveryCosts() )  );
    }
}
