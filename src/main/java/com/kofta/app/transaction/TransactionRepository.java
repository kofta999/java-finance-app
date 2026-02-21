package com.kofta.app.transaction;

import com.kofta.app.common.repository.Repository;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public interface TransactionRepository extends Repository<Transaction, UUID> {
    public List<Transaction> findAll(Predicate<Transaction> predicate);
    public void deleteById(UUID id);
}
