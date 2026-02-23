package com.kofta.app.transaction;

import java.util.List;

public interface TransactionService {
    Transaction create(TransactionDto dto);
    List<Transaction> findAll(TransactionFilter filter);
}
