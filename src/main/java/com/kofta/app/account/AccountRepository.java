package com.kofta.app.account;

import com.kofta.app.common.repository.Repository;
import java.util.List;
import java.util.UUID;

public interface AccountRepository extends Repository<Account, UUID> {
    List<Account> findByUserId(UUID userId);
}
