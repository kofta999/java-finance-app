package com.kofta.app.ui;

import com.kofta.app.finance.FinanceService;
import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.Transaction;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FinanceConsole {

    private final FinanceService service;
    private final List<Transaction> transactions;
    private final Scanner scanner;

    public FinanceConsole(
        FinanceService service,
        List<Transaction> transactions
    ) {
        this.service = service;
        this.transactions = transactions;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean isRunning = true;
        while (isRunning) {
            printMenu();

            System.out.print("Select a choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> printRemainingBalance();
                case "2" -> printSummaryByCategory();
                case "3" -> printFilterByCategory();
                case "4" -> isRunning = false;
                default -> System.out.println("Invalid Choice");
            }
        }

        scanner.close();
        System.out.println("Exiting...");
    }

    void printMenu() {
        System.out.println(
            """
            --- Finance Manager ---
            1. View Remaining Balance
            2. Show Summary (by category)
            3. Filter by category
            4. Exit
            """
        );
    }

    void printRemainingBalance() {
        System.out.println(
            String.format(
                "Remaining Balance: %10.2f $\n",
                service.calculateTotal(transactions)
            )
        );
    }

    void printSummaryByCategory() {
        var summaryMap = service.sumByCategory(transactions);
        var result = new StringBuilder("Summary By Category:\n");

        summaryMap.forEach((category, total) -> {
            result.append(String.format("%s: %10.2f $\n", category, total));
        });

        System.out.println(result.toString());
    }

    void printFilterByCategory() {
        System.out.print("Category: ");
        var category = Category.fromString(scanner.nextLine());

        if (category.isEmpty()) {
            System.out.println("Invalid Category.");
            return;
        }

        String result = service
            .filterByCategory(transactions, category.get())
            .stream()
            .map(Transaction::toString)
            .collect(Collectors.joining("\n"));

        System.out.println(result);
    }
}
