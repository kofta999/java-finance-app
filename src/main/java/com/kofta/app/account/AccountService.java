package com.kofta.app.account;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<Account> findByUserId(UUID userId);
}
