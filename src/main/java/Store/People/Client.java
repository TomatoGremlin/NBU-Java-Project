package Store.People;

import Store.Item;
import exeptions.IncorrectClientBudgetException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Client {
    private BigDecimal budget;
    private Map<Item, BigDecimal> items;

    public Client(BigDecimal budget, Map<Item, BigDecimal> items) {
        this.budget = budget;
        this.items = items;
    }

    public void setBudget(BigDecimal budget) throws IncorrectClientBudgetException {
        // if budget < 0
        if (budget.compareTo(BigDecimal.valueOf(0)) < 0){
            throw new IncorrectClientBudgetException("A client's budget should not be negative", budget);
        }
        this.budget = budget;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public Map<Item, BigDecimal> getItems() {
        return items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(budget, client.budget) && Objects.equals(items, client.items);
    }
    @Override
    public int hashCode() {
        return Objects.hash(budget, items);
    }
    @Override
    public String toString() {
        return "Client {" +
                "budget=" + budget +
                ", items=" + items +
                '}';
    }
}
