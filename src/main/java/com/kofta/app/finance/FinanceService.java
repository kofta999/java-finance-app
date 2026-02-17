package com.kofta.app.finance;

import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.Transaction;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FinanceService {
    public void initializeFromCsv(InputStream stream);
    public BigDecimal calculateTotal();
    public List<Transaction> filterByCategory(Category category);
    public Map<Category, BigDecimal> sumByCategory();
}
