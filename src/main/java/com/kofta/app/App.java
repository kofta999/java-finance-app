package com.kofta.app;

import com.kofta.app.account.Account;
import com.kofta.app.account.AccountRepository;
import com.kofta.app.account.AccountServiceImpl;
import com.kofta.app.account.InMemoryAccountRepository;
import com.kofta.app.finance.FinanceServiceImpl;
import com.kofta.app.transaction.CsvTransactionParser;
import com.kofta.app.transaction.InMemoryTransactionRepository;
import com.kofta.app.ui.FinanceConsole;
import com.kofta.app.user.InMemoryUserRepository;
import com.kofta.app.user.User;
import com.kofta.app.user.UserRepository;
import com.kofta.app.user.UserServiceImpl;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class App {

    /**
     *
     * @param userRepository
     * @param accountRepository
     * @return UUID The first account ID to put transactions in
     */
    static UUID generateSeed(
        UserRepository userRepository,
        AccountRepository accountRepository
    ) {
        var users = generateUsers();
        var accounts = generateAccounts(users.get(0).getId());

        users.forEach(userRepository::save);
        accounts.forEach(accountRepository::save);

        return accounts.get(0).getId();
    }

    static List<User> generateUsers() {
        return List.of(
            new User(UUID.randomUUID(), "User1"),
            new User(UUID.randomUUID(), "User2"),
            new User(UUID.randomUUID(), "User3")
        );
    }

    static List<Account> generateAccounts(UUID userId) {
        return List.of(
            new Account(UUID.randomUUID(), "Account1", "USD", userId),
            new Account(UUID.randomUUID(), "Account2", "JPY", userId),
            new Account(UUID.randomUUID(), "Account3", "EGP", userId)
        );
    }

    public static void main(String[] args) {
        try {
            InputStream stream = App.class.getClassLoader().getResourceAsStream(
                "input.csv"
            );

            if (stream == null) {
                throw new IOException("File not found in classpath");
            }

            var userRepository = new InMemoryUserRepository();
            var accountRepository = new InMemoryAccountRepository();
            var transactionRepository = new InMemoryTransactionRepository();

            var transactionParser = new CsvTransactionParser();

            var financeService = new FinanceServiceImpl(
                transactionRepository,
                transactionParser
            );
            var userService = new UserServiceImpl(userRepository);
            var accountService = new AccountServiceImpl(accountRepository);

            var accountId = generateSeed(userRepository, accountRepository);
            financeService.initializeFromCsv(stream, accountId);

            var financeConsole = new FinanceConsole(
                financeService,
                userService,
                accountService
            );

            financeConsole.start();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
