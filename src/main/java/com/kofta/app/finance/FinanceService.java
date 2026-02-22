package com.kofta.app.finance;

import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.Transaction;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FinanceService {
    void initializeFromCsv(InputStream stream, UUID accountId);
    List<Transaction> sortTransactionsBy(UUID accountId, TransactionSort sort);
    BigDecimal calculateTotal(UUID accountId);
    List<Transaction> filterByCategory(UUID accountId, Category category);
    Map<Category, BigDecimal> sumByCategory(UUID accountId);
}
