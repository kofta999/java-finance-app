package com.kofta.app.transaction;

import java.util.List;
import java.util.UUID;

public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction create(TransactionDto dto) {
        var transaction = new Transaction(
            UUID.randomUUID(),
            dto.date(),
            dto.description(),
            dto.amount(),
            dto.category(),
            dto.accountId()
        );

        transactionRepository.save(transaction).unwrap();

        return transaction;
    }

    @Override
    public List<Transaction> findAll(TransactionFilter filter) {
        return transactionRepository.findAll(filter);
    }
}
