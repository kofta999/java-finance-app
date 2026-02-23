package com.kofta.app.transaction;

import com.kofta.app.common.repository.Repository;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends Repository<Transaction, UUID> {
    public List<Transaction> findAll(TransactionFilter filter);
    public void deleteById(UUID id);
}
