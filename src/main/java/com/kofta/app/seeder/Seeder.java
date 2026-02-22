package com.kofta.app.seeder;

import com.kofta.app.account.Account;
import com.kofta.app.account.AccountRepository;
import com.kofta.app.common.result.Result;
import com.kofta.app.transaction.Transaction;
import com.kofta.app.transaction.TransactionParser;
import com.kofta.app.transaction.TransactionParsingError;
import com.kofta.app.transaction.TransactionRepository;
import com.kofta.app.user.User;
import com.kofta.app.user.UserRepository;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class Seeder {

    public static void seedFromCsv(
        TransactionRepository transactionRepository,
        TransactionParser transactionParser,
        InputStream stream,
        UUID accountId
    ) {
        switch (transactionParser.from(stream)) {
            case Result.Ok(var txs) -> txs.forEach(pt -> {
                var t = new Transaction(
                    UUID.randomUUID(),
                    pt.date(),
                    pt.description(),
                    pt.amount(),
                    pt.category(),
                    accountId
                );

                transactionRepository.save(t);
            });
            case Result.Err(var e) -> throw (TransactionParsingError) e;
        }
    }

    /**
     *
     * @param userRepository
     * @param accountRepository
     * @return UUID The first account ID to put transactions in
     */
    public static UUID generateSeed(
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
}
