package com.kofta.app.transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface TransactionRepository {
    Optional<Transaction> findById(UUID id);
    List<Transaction> findAll();
    List<Transaction> findAll(Predicate<Transaction> predicate);
    void save(Transaction transaction);
    void deleteById(UUID id);
}
