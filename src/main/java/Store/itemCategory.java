package Store;

public enum itemCategory {
    CONSUMABLE(0), NONCONSUMABLE(0);
    int percentageMarkup;

    itemCategory(int percentageMarkup) {
        this.percentageMarkup = percentageMarkup;
    }

    public int getPercentageMarkup() {
        return percentageMarkup;
    }

    public void setPercentageMarkup(int percentageMarkup) {
        this.percentageMarkup = percentageMarkup;
    }
}
