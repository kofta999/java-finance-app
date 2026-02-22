package com.kofta.app;

import com.kofta.app.account.Account;
import com.kofta.app.account.AccountRepository;
import com.kofta.app.account.AccountServiceImpl;
import com.kofta.app.account.SqliteAccountRepository;
import com.kofta.app.database.DatabaseConnectionManager;
import com.kofta.app.finance.FinanceServiceImpl;
import com.kofta.app.transaction.CsvTransactionParser;
import com.kofta.app.transaction.SqliteTransactionRepository;
import com.kofta.app.ui.FinanceConsole;
import com.kofta.app.user.SqliteUserRepository;
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
        var accounts = generateAccounts(users);

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

    static List<Account> generateAccounts(List<User> users) {
        return List.of(
            new Account(UUID.randomUUID(), "Main", "USD", users.get(0).getId()),
            new Account(
                UUID.randomUUID(),
                "Savings",
                "USD",
                users.get(0).getId()
            ),
            new Account(
                UUID.randomUUID(),
                "Checking",
                "JPY",
                users.get(1).getId()
            ),
            new Account(
                UUID.randomUUID(),
                "Business",
                "EGP",
                users.get(2).getId()
            )
        );
    }

    public static void main(String[] args) {
        try (
            var dbManager = new DatabaseConnectionManager(
                "jdbc:sqlite:finance.db"
            )
        ) {
            InputStream stream = App.class.getClassLoader().getResourceAsStream(
                "input.csv"
            );

            if (stream == null) {
                throw new IOException("File not found in classpath");
            }

            // var userRepository = new InMemoryUserRepository();
            // var accountRepository = new InMemoryAccountRepository();
            // var transactionRepository = new InMemoryTransactionRepository();
            var connection = dbManager.getConnection();
            var userRepository = new SqliteUserRepository(connection);
            var accountRepository = new SqliteAccountRepository(connection);
            var transactionRepository = new SqliteTransactionRepository(
                connection
            );

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
