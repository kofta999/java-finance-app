package com.kofta.app.finance;

import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.Transaction;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FinanceService {

    public double calculateTotal(List<Transaction> transactions) {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    public List<Transaction> filterByCategory(
        List<Transaction> transactions,
        Category category
    ) {
        return transactions
            .stream()
            .filter(t -> t.getCategory() == category)
            .toList();
    }

    public Map<Category, Double> sumByCategory(List<Transaction> transactions) {
        return transactions
            .stream()
            .collect(
                Collectors.groupingBy(
                    Transaction::getCategory,
                    Collectors.summingDouble(Transaction::getAmount)
                )
            );
    }
}
