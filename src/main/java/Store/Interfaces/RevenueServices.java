package Store.Interfaces;

public interface RevenueServices {
    // 1 . Calculate the sum of all the items sold
    public double calculateItemsSoldRevenue();

    // 2. Calculate the costs for the salaries of the employees
    public double calculateCashiersSalaries();

    // 3. Calculate the sum the delivery will cost
    public double calculateDeliveryCosts();

    // 4. Calculate the total revenue
    public default double calculateTotalRevenue(){
        return calculateItemsSoldRevenue() - (calculateCashiersSalaries() + calculateDeliveryCosts());
    }
}
