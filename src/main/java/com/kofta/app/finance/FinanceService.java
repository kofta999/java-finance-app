package com.kofta.app.finance;

import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.Transaction;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FinanceService {

    public BigDecimal calculateTotal(List<Transaction> transactions) {
        return transactions
            .stream()
            .map(Transaction::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Transaction> filterByCategory(
        List<Transaction> transactions,
        Category category
    ) {
        return transactions
            .stream()
            .filter(t -> t.category() == category)
            .toList();
    }

    public Map<Category, BigDecimal> sumByCategory(
        List<Transaction> transactions
    ) {
        return transactions
            .stream()
            .collect(
                Collectors.groupingBy(
                    Transaction::category,
                    Collectors.mapping(
                        Transaction::amount,
                        Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                    )
                )
            );
    }
}
