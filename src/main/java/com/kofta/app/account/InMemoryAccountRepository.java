package com.kofta.app.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryAccountRepository implements AccountRepository {

    private HashMap<UUID, Account> map;

    public InMemoryAccountRepository() {
        this.map = new HashMap<>();
    }

    @Override
    public Optional<Account> findById(UUID accountId) {
        return Optional.ofNullable(map.get(accountId));
    }

    @Override
    public List<Account> findByUserId(UUID userId) {
        return map
            .values()
            .stream()
            .filter(a -> a.getUserId().equals(userId))
            .toList();
    }

    @Override
    public void save(Account account) {
        map.put(account.getId(), account);
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(map.values());
    }
}
