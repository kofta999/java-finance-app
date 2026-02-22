package com.kofta.app.finance;

import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.Transaction;
import com.kofta.app.transaction.TransactionRepository;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FinanceServiceImpl implements FinanceService {

    private TransactionRepository transactionRepository;

    public FinanceServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public BigDecimal calculateTotal(UUID accountId) {
        return this.transactionRepository.findAll()
            .stream()
            .filter(t -> t.accountId().equals(accountId))
            .map(Transaction::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Transaction> filterByCategory(
        UUID accountId,
        Category category
    ) {
        return transactionRepository.findAll(
            t -> t.category() == category && t.accountId().equals(accountId)
        );
    }

    @Override
    public Map<Category, BigDecimal> sumByCategory(UUID accountId) {
        return transactionRepository
            .findAll(t -> t.accountId().equals(accountId))
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

    @Override
    public List<Transaction> sortTransactionsBy(
        UUID accountId,
        TransactionSort sort
    ) {
        Comparator<Transaction> comparator = switch (sort) {
            case AMOUNT -> Comparator.comparing(Transaction::amount);
            case DATE -> Comparator.comparing(Transaction::date);
            case CATEGORY -> Comparator.comparing(Transaction::category);
        };

        return transactionRepository
            .findAll(t -> t.accountId().equals(accountId))
            .stream()
            .sorted(comparator)
            .toList();
    }
}
