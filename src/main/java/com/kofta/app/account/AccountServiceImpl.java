package com.kofta.app.account;

import java.util.List;
import java.util.UUID;

public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Account> findByUserId(UUID userId) {
        return accountRepository.findByUserId(userId);
    }
}
