package Store.enums;

public enum ItemCategory {
    CONSUMABLE(0), NONCONSUMABLE(0);
    int percentageMarkup;

    ItemCategory(int percentageMarkup) {
        this.percentageMarkup = percentageMarkup;
    }

    public int getPercentageMarkup() {
        return percentageMarkup;
    }

    public void setPercentageMarkup(int percentageMarkup) {
        this.percentageMarkup = percentageMarkup;
    }
}
