package com.kofta.app.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CsvTransactionParserTest {

    private CsvTransactionParser parser;

    @BeforeEach
    void setUp() {
        parser = new CsvTransactionParser();
    }

    @Test
    @DisplayName("Should parse valid CSV content")
    void testParse() {
        var content =
            "date,description,amount,category\n" + "2024-01-01,test,10.5,FOOD";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        var result = parser.from(inputStream);
        assertEquals(1, result.size());
        var transaction = result.get(0);
        assertEquals(LocalDate.of(2024, 1, 1), transaction.date());
        assertEquals("test", transaction.description());
        assertEquals(new BigDecimal("10.5"), transaction.amount());
        assertEquals(Category.FOOD, transaction.category());
    }

    @Test
    @DisplayName("Should return an empty list for malformed CSV")
    void testParseMalformed() {
        var content =
            "date,description,amount,category\n" + "2024-01-01,test,10.5";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        var result = parser.from(inputStream);
        assertEquals(1, result.size());
        var transaction = result.get(0);
        assertEquals(LocalDate.of(2024, 1, 1), transaction.date());
        assertEquals("test", transaction.description());
        assertEquals(new BigDecimal("10.5"), transaction.amount());
        assertEquals(null, transaction.category());
    }

    @Test
    @DisplayName("Should return an empty list for empty CSV")
    void testParseEmpty() {
        var content = "";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        var result = parser.from(inputStream);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should handle CSV with only headers")
    void testParseOnlyHeaders() {
        var content = "date,description,amount,category";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        var result = parser.from(inputStream);
        assertEquals(0, result.size());
    }
}
