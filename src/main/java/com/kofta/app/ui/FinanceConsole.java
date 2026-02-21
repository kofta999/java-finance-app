package com.kofta.app.ui;

import com.kofta.app.account.Account;
import com.kofta.app.account.AccountService;
import com.kofta.app.finance.FinanceService;
import com.kofta.app.transaction.Category;
import com.kofta.app.user.User;
import com.kofta.app.user.UserService;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class FinanceConsole {

    private final FinanceService financeService;
    private final UserService userService;
    private final AccountService accountService;
    private final Scanner scanner;
    private User currentUser;
    private Account currentAccount;

    public FinanceConsole(
        FinanceService financeService,
        UserService userService,
        AccountService accountService
    ) {
        this.financeService = financeService;
        this.userService = userService;
        this.accountService = accountService;
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
        this.currentAccount = null;
    }

    public void start() {
        selectUser();
        selectAccount();
        mainLoop();

        scanner.close();
        System.out.println("Exiting...");
    }

    void mainLoop() {
        boolean isRunning = true;
        while (isRunning) {
            printHeader();
            System.out.print("Select a choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> printRemainingBalance();
                // case "2" -> sortAndPrintAllTransactions();
                case "2" -> printSummaryByCategory();
                case "3" -> printFilterByCategory();
                case "4" -> selectAccount();
                case "5" -> {
                    selectUser();
                    selectAccount();
                }
                case "6" -> isRunning = false;
                default -> System.out.println("Invalid Choice");
            }
        }
    }

    // void sortAndPrintAllTransactions() {
    //     // TODO
    //     var fields = List.of("Amount", "Date", "Category");
    // }

    void selectAccount() {
        var accounts = accountService.findByUserId(currentUser.getId());
        this.currentAccount = promptForSelection(
            "Select Account: ",
            accounts,
            Account::getName
        );
    }

    void selectUser() {
        var users = userService.findAll();
        this.currentUser = promptForSelection(
            "Select a User: ",
            users,
            User::getName
        );
    }

    void printHeader() {
        System.out.print(
            """
            --- Finance Manager ---
            --- Welcome %s, account: %s

            1. View Remaining Balance
            2. Show Summary (by category)
            3. Filter by category
            4. Switch account
            5. Switch user
            6. Exit
            -----------------------
            """.formatted(currentUser.getName(), currentAccount.getName())
        );
    }

    void printRemainingBalance() {
        var balance = financeService.calculateTotal(currentAccount.getId());
        System.out.printf("Remaining Balance: %,.2f $%n%n", balance);
    }

    void printSummaryByCategory() {
        System.out.println("--- Summary By Category ---");
        financeService
            .sumByCategory(currentAccount.getId())
            .forEach((cat, total) ->
                System.out.printf("%-15s: %10.2f $%n", cat, total)
            );
    }

    void printFilterByCategory() {
        List<Category> categories = Arrays.asList(Category.values());
        var selected = promptForSelection(
            "Enter category (or 'cancel'): ",
            categories,
            Category::toString
        );

        if (selected == null) return;

        var transactions = financeService.filterByCategory(
            currentAccount.getId(),
            selected
        );

        if (transactions.isEmpty()) {
            System.out.println("No transactions found for " + selected);
        } else {
            transactions.forEach(System.out::println);
        }
    }

    private <T> T promptForSelection(
        String prompt,
        List<T> items,
        Function<T, String> formatter
    ) {
        while (true) {
            for (int i = 0; i < items.size(); i++) {
                System.out.printf(
                    "%d. %s%n",
                    i + 1,
                    formatter.apply(items.get(i))
                );
            }
            System.out.print(prompt);
            try {
                int index = Integer.parseInt(scanner.nextLine()) - 1;
                return items.get(index);
            } catch (Exception e) {
                System.out.println("Invalid selection. Please try again.");
            }
        }
    }
}
