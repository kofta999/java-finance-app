package com.kofta.app;

import com.kofta.app.account.AccountServiceImpl;
import com.kofta.app.account.SqliteAccountRepository;
import com.kofta.app.api.ApiServer;
import com.kofta.app.api.handler.AccountHandler;
import com.kofta.app.api.handler.TransactionHandler;
import com.kofta.app.api.handler.UserHandler;
import com.kofta.app.database.DatabaseConnectionManager;
import com.kofta.app.finance.FinanceServiceImpl;
import com.kofta.app.seeder.Seeder;
import com.kofta.app.transaction.CsvTransactionParser;
import com.kofta.app.transaction.SqliteTransactionRepository;
import com.kofta.app.transaction.TransactionServiceImpl;
import com.kofta.app.ui.FinanceConsole;
import com.kofta.app.user.SqliteUserRepository;
import com.kofta.app.user.UserServiceImpl;
import java.io.IOException;
import java.io.InputStream;

public class App {

    public static void main(String[] args) {
        try (
            var dbManager = new DatabaseConnectionManager(
                "jdbc:sqlite:finance.db?journal_mode=WAL"
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
            var userRepository = new SqliteUserRepository(dbManager);
            var accountRepository = new SqliteAccountRepository(dbManager);
            var transactionRepository = new SqliteTransactionRepository(
                dbManager
            );

            var transactionParser = new CsvTransactionParser();

            var financeService = new FinanceServiceImpl(transactionRepository);
            var userService = new UserServiceImpl(userRepository);
            var accountService = new AccountServiceImpl(accountRepository);
            var transactionService = new TransactionServiceImpl(
                transactionRepository
            );

            // Surely remove this in prod, just for testing
            if (userService.findAll().isEmpty()) {
                var accountId = Seeder.generateSeed(
                    userRepository,
                    accountRepository
                );
                Seeder.seedFromCsv(
                    transactionRepository,
                    transactionParser,
                    stream,
                    accountId
                );
            }

            var apiServer = new ApiServer(3000);
            apiServer.start();

            apiServer.registerHandler("/users", new UserHandler(userService));
            apiServer.registerHandler(
                "/accounts",
                new AccountHandler(accountService, financeService)
            );
            apiServer.registerHandler(
                "/transactions",
                new TransactionHandler(transactionService)
            );

            // NOTE: Reading from stdin here prevents main from finishing (as ApiServer runs in a separate background thread)
            // so that makes dbManager accessible to ApiServer without removing try-with-resources block
            System.out.println("Press Enter to stop server...");
            System.in.read();

            apiServer.stop();

            // var financeConsole = new FinanceConsole(
            //     financeService,
            //     userService,
            //     accountService
            // );

            // financeConsole.start();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
