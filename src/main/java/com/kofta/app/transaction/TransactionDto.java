package com.kofta.app.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionDto(
    LocalDate date,
    String description,
    BigDecimal amount,
    Category category,
    UUID accountId
) {}
