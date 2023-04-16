package model;

import javafx.beans.property.*;

public class Budget {
    private final StringProperty category;
    private final DoubleProperty amount;
    private final DoubleProperty remainingAmount;

    public Budget(String category, double amount) {
        this.category = new SimpleStringProperty(category);
        this.amount = new SimpleDoubleProperty(amount);
        this.remainingAmount = new SimpleDoubleProperty(amount);
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public double getRemainingAmount() {
        return remainingAmount.get();
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount.set(remainingAmount);
    }

    public DoubleProperty remainingAmountProperty() {
        return remainingAmount;
    }
}
