package model;

import java.time.LocalDate;

public class Expenditure {
    private LocalDate date;
    private String description;
    private double amount;
    private String category;
    private boolean recurring;
    private String dateStr;

    public Expenditure(LocalDate date, double amount, String description, String category, boolean recurring) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category; 
        this.recurring = recurring;
    }
    
    public Expenditure(LocalDate date, double amount, String category, String description) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }


    public Expenditure(String date, double amount, String category, String description) {
    	 this.dateStr = date;
         this.amount = amount;
         this.category = category;
         this.description = description;
	}
    
    public Expenditure(Expenditure other) {
        this.date = other.date;
        this.amount = other.amount;
        this.description = other.description;
        this.category = other.category;
        this.recurring = other.recurring;
    }

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }
}
