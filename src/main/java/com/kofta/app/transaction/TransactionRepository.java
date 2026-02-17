package com.kofta.app.transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface TransactionRepository {
    public Optional<Transaction> findById(UUID id);
    public List<Transaction> findAll();
    public List<Transaction> findAll(Predicate<Transaction> predicate);
    public void save(Transaction transaction);
    public void deleteById(UUID id);
}
