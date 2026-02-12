package com.kofta.app;

import com.kofta.app.transaction.TransactionParser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args)
        throws IOException, URISyntaxException {
        var filePath = Paths.get(
            App.class.getClassLoader().getResource("input.csv").toURI()
        );

        var transactions = TransactionParser.fromCsvFile(filePath);

        transactions.forEach(t -> System.out.println(t));
    }
}
