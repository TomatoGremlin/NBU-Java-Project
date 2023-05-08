package Store.People;

import Store.Item;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Client {
    private double budget;
    private Map<Item, Integer> items;

    public Client(double budget, Map<Item, Integer> items) {
        this.budget = budget;
        this.items = items;
    }

    public double getBudget() {
        return budget;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }


    @Override
    public String toString() {
        return "Client {" +
                "budget=" + budget +
                ", items=" + items +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Double.compare(client.budget, budget) == 0 && Objects.equals(items, client.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(budget, items);
    }


}
