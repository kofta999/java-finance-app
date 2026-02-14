package com.kofta.app.transaction;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionParserTest {

    private final Path resources = Paths.get("src", "test", "resources");

    @Test
    @DisplayName("Should parse a valid CSV file correctly")
    void testParseValidCsv() throws TransactionParsingException, IOException {
        var filePath = resources.resolve("valid-transactions.csv");
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            var transactions = TransactionParser.fromCsvFile(inputStream);

            var expected = List.of(
                new Transaction(
                    LocalDate.of(2023, 1, 15),
                    "Groceries",
                    50.0,
                    Category.FOOD
                ),
                new Transaction(
                    LocalDate.of(2023, 1, 16),
                    "New shirt",
                    -25.5,
                    Category.SHOPPING
                ),
                new Transaction(
                    LocalDate.of(2023, 1, 17),
                    "Monthly Salary",
                    2500.0,
                    Category.SALARY
                )
            );

            assertEquals(expected, transactions);
        }
    }

    @Test
    @DisplayName("Should throw exception for a closed stream")
    void testParseClosedStream() throws IOException {
        var filePath = resources.resolve("valid-transactions.csv");
        InputStream inputStream = Files.newInputStream(filePath);
        inputStream.close();
        assertThrows(TransactionParsingException.class, () ->
            TransactionParser.fromCsvFile(inputStream)
        );
    }

    @Test
    @DisplayName("Should throw exception for a malformed CSV")
    void testParseMalformedCsv() throws IOException {
        var filePath = resources.resolve("malformed-transactions.csv");
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            assertThrows(TransactionParsingException.class, () ->
                TransactionParser.fromCsvFile(inputStream)
            );
        }
    }

    @Test
    @DisplayName("Should return empty list for an empty CSV file")
    void testParseEmptyCsv() throws TransactionParsingException, IOException {
        var filePath = resources.resolve("empty.csv");
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            var transactions = TransactionParser.fromCsvFile(inputStream);
            assertTrue(transactions.isEmpty());
        }
    }

    @Test
    @DisplayName("Should return empty list for a CSV with only headers")
    void testParseCsvWithOnlyHeaders()
        throws TransactionParsingException, IOException {
        var filePath = resources.resolve("only-headers.csv");
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            var transactions = TransactionParser.fromCsvFile(inputStream);
            assertTrue(transactions.isEmpty());
        }
    }

    @Test
    @DisplayName("Should throw an exception for a null input stream")
    void testParseWithNullInputStream() {
        assertThrows(IllegalArgumentException.class, () ->
            TransactionParser.fromCsvFile(null)
        );
    }
}
