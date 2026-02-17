package com.kofta.app;

import com.kofta.app.finance.FinanceServiceImpl;
import com.kofta.app.transaction.CsvTransactionParser;
import com.kofta.app.transaction.InMemoryTransactionRepository;
import com.kofta.app.ui.FinanceConsole;
import java.io.IOException;
import java.io.InputStream;

public class App {

    public static void main(String[] args) {
        try {
            InputStream stream = App.class.getClassLoader().getResourceAsStream(
                "input.csv"
            );

            if (stream == null) {
                throw new IOException("File not found in classpath");
            }

            var financeService = new FinanceServiceImpl(
                new InMemoryTransactionRepository(),
                new CsvTransactionParser()
            );

            financeService.initializeFromCsv(stream);

            var financeConsole = new FinanceConsole(financeService);

            financeConsole.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
