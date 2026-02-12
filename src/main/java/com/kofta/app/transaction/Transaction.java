package com.kofta.app.transaction;

import java.time.LocalDate;
import java.util.Objects;

public class Transaction {

    private LocalDate date;
    private String description;
    private double amount;
    private Category category;

    public Transaction() {
        this.date = LocalDate.now();
        this.description = null;
        this.amount = 0;
        this.category = null;
    }

    public Transaction(
        LocalDate date,
        String description,
        double amount,
        Category category
    ) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (
            !(obj instanceof Transaction t) || getClass() != t.getClass()
        ) return false;

        return (
            Objects.equals(this.date, t.date) &&
            Objects.equals(this.description, t.description) &&
            this.amount == t.amount &&
            Objects.equals(this.category, t.category)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, description, amount, category);
    }

    @Override
    public String toString() {
        return String.format(
            "%-12s | %-30s | %10.2f | %-10s",
            date,
            description,
            amount,
            category
        );
    }
}
