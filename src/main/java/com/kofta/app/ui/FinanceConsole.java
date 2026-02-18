package com.kofta.app.ui;

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
    private final Scanner scanner;
    private User currentUser;

    public FinanceConsole(
        FinanceService financeService,
        UserService userService
    ) {
        this.financeService = financeService;
        this.userService = userService;
        this.scanner = new Scanner(System.in);
        this.currentUser = null;
    }

    public void start() {
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
                case "5" -> isRunning = false;
                default -> System.out.println("Invalid Choice");
            }
        }

        scanner.close();
        System.out.println("Exiting...");
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

    void printMenu() {
        System.out.println(
            String.format(
                """
                --- Finance Manager ---
                --- Welcome %s

                1. View Remaining Balance
                2. Show Summary (by category)
                3. Filter by category
                4. Switch account
                5. Exit
                -----------------------
                """,
                currentUser.getName()
            )
        );
    }

    void printRemainingBalance() {
        System.out.println(
            String.format(
                "Remaining Balance: %10.2f $\n",
                financeService.calculateTotal()
            )
        );
    }

    void printSummaryByCategory() {
        var summaryMap = financeService.sumByCategory();
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
            .filterByCategory(category)
            .stream()
            .map(Transaction::toString)
            .collect(Collectors.joining("\n"));

        System.out.println(result);
        System.out.println();
    }
}
