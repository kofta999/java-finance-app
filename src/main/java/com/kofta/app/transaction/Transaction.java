package com.kofta.app.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record Transaction(
    UUID id,
    LocalDate date,
    String description,
    BigDecimal amount,
    Category category,
    UUID accountId
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
