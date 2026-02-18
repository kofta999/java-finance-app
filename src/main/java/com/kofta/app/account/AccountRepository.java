package com.kofta.app.account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findById(UUID accountId);
    List<Account> findByUserId(UUID userId);
    void save(Account account);
}
