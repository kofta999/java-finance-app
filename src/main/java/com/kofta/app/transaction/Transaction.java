package com.kofta.app.transaction;

import java.time.LocalDate;

public record Transaction(
    LocalDate date,
    String description,
    double amount,
    Category category
) {
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
