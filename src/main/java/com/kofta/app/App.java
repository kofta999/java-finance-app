package com.kofta.app;

import com.kofta.app.finance.FinanceServiceImpl;
import com.kofta.app.transaction.TransactionParser;
import com.kofta.app.ui.FinanceConsole;
import java.io.IOException;

public class App {

    public static void main(String[] args) {
        try {
            var stream = App.class.getClassLoader().getResourceAsStream(
                "input.csv"
            );

            if (stream == null) {
                throw new IOException("File not found in classpath");
            }

            var transactions = TransactionParser.fromCsvFile(stream);
            var financeConsole = new FinanceConsole(
                new FinanceServiceImpl(),
                transactions
            );

            financeConsole.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
