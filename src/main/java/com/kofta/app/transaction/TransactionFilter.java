package com.kofta.app.transaction;

import java.util.UUID;

public record TransactionFilter(UUID accountId, Category category) {}
