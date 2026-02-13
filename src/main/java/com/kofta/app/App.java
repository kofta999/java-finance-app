package com.kofta.app;

import com.kofta.app.finance.FinanceService;
import com.kofta.app.transaction.TransactionParser;
import com.kofta.app.ui.FinanceConsole;
import java.nio.file.Paths;

public class App {

    public static void main(String[] args) {
        try {
            var filePath = Paths.get(
                App.class.getClassLoader().getResource("input.csv").toURI()
            );

            var transactions = TransactionParser.fromCsvFile(filePath);
            var financeConsole = new FinanceConsole(
                new FinanceService(),
                transactions
            );

            financeConsole.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
