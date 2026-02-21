package com.kofta.app.transaction;

import com.kofta.app.common.repository.EntityNotFoundError;
import com.kofta.app.common.result.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class InMemoryTransactionRepository implements TransactionRepository {

    private HashMap<UUID, Transaction> map;

    public InMemoryTransactionRepository() {
        this.map = new HashMap<>();
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public List<Transaction> findAll(Predicate<Transaction> predicate) {
        return map.values().stream().filter(predicate).toList();
    }

    @Override
    public Result<Void, EntityNotFoundError> save(Transaction transaction) {
        map.put(transaction.id(), transaction);
        return new Result.Ok<>(null);
    }

    @Override
    public void deleteById(UUID id) {
        map.remove(id);
    }
}
