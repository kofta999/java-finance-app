package com.kofta.app.ui;

import com.kofta.app.account.Account;
import com.kofta.app.account.AccountService;
import com.kofta.app.finance.FinanceService;
import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.Transaction;
import com.kofta.app.user.User;
import com.kofta.app.user.UserService;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
            printMenu();

            System.out.print("Select a choice: ");
            String choice = scanner.nextLine();
            System.out.println();

            switch (choice) {
                case "1" -> printRemainingBalance();
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

    void selectAccount() {
        boolean isAccountSelectionRunning = true;
        var accountList = accountService.findByUserId(currentUser.getId());

        while (isAccountSelectionRunning) {
            printAccountSelectionMenu(accountList);

            System.out.print("Select an Account: ");
            String choice = scanner.nextLine();
            System.out.println();

            try {
                int accountIndex = Integer.parseInt(choice);
                this.currentAccount = accountList.get(accountIndex - 1);
                isAccountSelectionRunning = false;
            } catch (Exception e) {
                System.out.println("Invalid Account, try again.");
            }
        }
    }

    void selectUser() {
        boolean isUserSelectionRunning = true;
        var userList = userService.findAll();

        while (isUserSelectionRunning) {
            printUserSelectionMenu(userList);

            System.out.print("Select a User: ");
            String choice = scanner.nextLine();
            System.out.println();

            try {
                int userIndex = Integer.parseInt(choice);
                this.currentUser = userList.get(userIndex - 1);
                isUserSelectionRunning = false;
            } catch (Exception e) {
                System.out.println("Invalid User, try again.");
            }
        }
    }

    void printUserSelectionMenu(List<User> userList) {
        var res = new StringBuilder();
        res.append(
            """
            --- Finance Manager ---
            -------- Login --------
            """
        );

        for (int i = 0; i < userList.size(); i++) {
            res.append(
                String.format("%d. %s\n", i + 1, userList.get(i).getName())
            );
        }

        res.append(
            """
            -----------------------

            """
        );

        System.out.println(res);
    }

    void printAccountSelectionMenu(List<Account> accountList) {
        var res = new StringBuilder();
        res.append(
            """
            --- Finance Manager ---
            --- Select Account ----
            """
        );

        for (int i = 0; i < accountList.size(); i++) {
            res.append(
                String.format("%d. %s\n", i + 1, accountList.get(i).getName())
            );
        }

        res.append(
            """
            -----------------------

            """
        );

        System.out.println(res);
    }

    void printMenu() {
        System.out.println(
            String.format(
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
                """,
                currentUser.getName(),
                currentAccount.getName()
            )
        );
    }

    void printRemainingBalance() {
        System.out.println(
            String.format(
                "Remaining Balance: %10.2f $\n",
                financeService.calculateTotal(currentAccount.getId())
            )
        );
    }

    void printSummaryByCategory() {
        var summaryMap = financeService.sumByCategory(currentAccount.getId());
        var result = new StringBuilder("Summary By Category:\n");

        summaryMap.forEach((category, total) -> {
            result.append(String.format("%s: %10.2f $\n", category, total));
        });

        System.out.println(result.toString());
    }

    private Category askForCategory() {
        while (true) {
            System.out.println("Enter category (or 'cancel'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) return null;

            var category = Category.fromString(input);
            if (category.isPresent()) return category.get();

            System.out.println("Invalid Category. Try: FOOD, RENT, etc.");
        }
    }

    void printFilterByCategory() {
        var category = askForCategory();

        if (category == null) return;

        String result = financeService
            .filterByCategory(currentAccount.getId(), category)
            .stream()
            .map(Transaction::toString)
            .collect(Collectors.joining("\n"));

        System.out.println(result);
        System.out.println();
    }
}
