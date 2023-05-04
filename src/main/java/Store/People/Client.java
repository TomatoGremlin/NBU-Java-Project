package Store.People;

import Store.Item;

import java.util.List;
import java.util.Map;

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
}
