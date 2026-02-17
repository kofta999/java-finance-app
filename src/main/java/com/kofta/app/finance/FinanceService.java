package com.kofta.app.finance;

import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.Transaction;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FinanceService {
    public BigDecimal calculateTotal(List<Transaction> transactions);

    public List<Transaction> filterByCategory(
        List<Transaction> transactions,
        Category category
    );

    public Map<Category, BigDecimal> sumByCategory(
        List<Transaction> transactions
    );
}
