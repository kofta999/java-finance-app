package com.kofta.app.transaction;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
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
    void testParseValidCsv() throws TransactionParsingException {
        var filePath = resources.resolve("valid-transactions.csv");
        var transactions = TransactionParser.fromCsvFile(filePath);

        var expected = List.of(
            new Transaction(LocalDate.of(2023, 1, 15), "Groceries", 50.0, Category.FOOD),
            new Transaction(LocalDate.of(2023, 1, 16), "New shirt", -25.5, Category.SHOPPING),
            new Transaction(LocalDate.of(2023, 1, 17), "Monthly Salary", 2500.0, Category.SALARY)
        );

        assertEquals(expected, transactions);
    }

    @Test
    @DisplayName("Should throw exception for a non-existent file")
    void testParseNonExistentFile() {
        var filePath = resources.resolve("non-existent-file.csv");
        assertThrows(TransactionParsingException.class, () -> TransactionParser.fromCsvFile(filePath));
    }

    @Test
    @DisplayName("Should throw exception for a malformed CSV")
    void testParseMalformedCsv() {
        var filePath = resources.resolve("malformed-transactions.csv");
        assertThrows(TransactionParsingException.class, () -> TransactionParser.fromCsvFile(filePath));
    }

    @Test
    @DisplayName("Should return empty list for an empty CSV file")
    void testParseEmptyCsv() throws TransactionParsingException {
        var filePath = resources.resolve("empty.csv");
        var transactions = TransactionParser.fromCsvFile(filePath);
        assertTrue(transactions.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list for a CSV with only headers")
    void testParseCsvWithOnlyHeaders() throws TransactionParsingException {
        var filePath = resources.resolve("only-headers.csv");
        var transactions = TransactionParser.fromCsvFile(filePath);
        assertTrue(transactions.isEmpty());
    }

    @Test
    @DisplayName("Should throw an exception for a null path")
    void testParseWithNullPath() {
        assertThrows(NullPointerException.class, () -> TransactionParser.fromCsvFile(null));
    }
}
