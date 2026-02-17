package com.kofta.app.transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParsedTransaction(
    LocalDate date,
    String description,
    BigDecimal amount,
    Category category
) {}
