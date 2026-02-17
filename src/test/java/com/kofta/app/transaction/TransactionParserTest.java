package com.kofta.app.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionParserTest {

    private final Path resources = Paths.get("src", "test", "resources");
    private TransactionParser parser;

    @BeforeEach
    void setUp() {
        parser = new CsvTransactionParser();
    }

    @Test
    @DisplayName("Should parse a valid CSV file correctly")
    void testParseValidCsv() throws TransactionParsingException, IOException {
        var filePath = resources.resolve("valid-transactions.csv");
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            var transactions = parser.from(inputStream);

            var expected = List.of(
                new ParsedTransaction(
                    LocalDate.of(2023, 1, 15),
                    "Groceries",
                    BigDecimal.valueOf(50.0),
                    Category.FOOD
                ),
                new ParsedTransaction(
                    LocalDate.of(2023, 1, 16),
                    "New shirt",
                    BigDecimal.valueOf(-25.5),
                    Category.SHOPPING
                ),
                new ParsedTransaction(
                    LocalDate.of(2023, 1, 17),
                    "Monthly Salary",
                    BigDecimal.valueOf(2500.0),
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
        // The read operation on a closed stream will throw an IOException, which our parser wraps
        assertThrows(TransactionParsingException.class, () ->
            parser.from(inputStream)
        );
    }

    @Test
    @DisplayName("Should throw exception for a malformed CSV")
    void testParseMalformedCsv() throws IOException {
        var filePath = resources.resolve("malformed-transactions.csv");
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            assertThrows(TransactionParsingException.class, () ->
                parser.from(inputStream)
            );
        }
    }

    @Test
    @DisplayName("Should return empty list for an empty CSV file")
    void testParseEmptyCsv() throws TransactionParsingException, IOException {
        var filePath = resources.resolve("empty.csv");
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            var transactions = parser.from(inputStream);
            assertTrue(transactions.isEmpty());
        }
    }

    @Test
    @DisplayName("Should return empty list for a CSV with only headers")
    void testParseCsvWithOnlyHeaders()
        throws TransactionParsingException, IOException {
        var filePath = resources.resolve("only-headers.csv");
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            var transactions = parser.from(inputStream);
            assertTrue(transactions.isEmpty());
        }
    }

    @Test
    @DisplayName("Should throw an exception for a null input stream")
    void testParseWithNullInputStream() {
        assertThrows(IllegalArgumentException.class, () -> parser.from(null));
    }
}
