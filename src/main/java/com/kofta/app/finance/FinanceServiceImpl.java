package com.kofta.app.finance;

import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.Transaction;
import com.kofta.app.transaction.TransactionParser;
import com.kofta.app.transaction.TransactionRepository;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FinanceServiceImpl implements FinanceService {

    private TransactionRepository transactionRepository;
    private TransactionParser transactionParser;

    public FinanceServiceImpl(
        TransactionRepository transactionRepository,
        TransactionParser transactionParser
    ) {
        this.transactionRepository = transactionRepository;
        this.transactionParser = transactionParser;
    }

    @Override
    public void initializeFromCsv(InputStream stream) {
        transactionParser
            .from(stream)
            .forEach(pt -> {
                var t = new Transaction(
                    UUID.randomUUID(),
                    pt.date(),
                    pt.description(),
                    pt.amount(),
                    pt.category()
                );

                transactionRepository.save(t);
            });
    }

    @Override
    public BigDecimal calculateTotal() {
        return this.transactionRepository.findAll()
            .stream()
            .map(Transaction::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Transaction> filterByCategory(Category category) {
        return transactionRepository.findAll(t -> t.category() == category);
    }

    @Override
    public Map<Category, BigDecimal> sumByCategory() {
        return transactionRepository
            .findAll()
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
