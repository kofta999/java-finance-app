package com.kofta.app.finance;

import static org.junit.jupiter.api.Assertions.*;

import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;

class FinanceServiceTest {

    private FinanceService service;
    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        service = new FinanceService();
        transactions = List.of(
            new Transaction(
                LocalDate.now(),
                "a",
                BigDecimal.valueOf(10),
                Category.FOOD
            ),
            new Transaction(
                LocalDate.now(),
                "b",
                BigDecimal.valueOf(-12),
                Category.FOOD
            ),
            new Transaction(
                LocalDate.now(),
                "c",
                BigDecimal.valueOf(20),
                Category.HEALTH
            ),
            new Transaction(
                LocalDate.now(),
                "d",
                BigDecimal.valueOf(-30),
                Category.SHOPPING
            ),
            new Transaction(
                LocalDate.now(),
                "e",
                BigDecimal.valueOf(18),
                Category.SHOPPING
            )
        );
    }

    @Test
    @DisplayName("Total should be 0 when list is empty")
    void testEmptyList() {
        var result = service.calculateTotal(List.of());
        assertEquals(
            BigDecimal.valueOf(0),
            result,
            "Total of empty list must be 0"
        );
    }

    @Test
    @DisplayName("CalculateTotal should throw on null input")
    void testCalculateTotalWithNullList() {
        assertThrows(NullPointerException.class, () ->
            service.calculateTotal(null)
        );
    }

    @Test
    @DisplayName("Should calculate results correctly")
    void testList() {
        var result = service.calculateTotal(transactions);
        assertEquals(
            BigDecimal.valueOf(6),
            result,
            "Total of input list must be 6"
        );
    }

    @Test
    @DisplayName("Get all shopping transactions")
    void testShoppingCategory() {
        var result = service.filterByCategory(transactions, Category.SHOPPING);
        assertEquals(List.of(transactions.get(3), transactions.get(4)), result);
    }

    @Test
    @DisplayName("Use a category not in list, should get empty list")
    void testNonExistentCategory() {
        var result = service.filterByCategory(transactions, Category.RENT);
        assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Sum by category")
    void testSumByCategory() {
        var result = service.sumByCategory(transactions);
        assertEquals(
            Map.of(
                Category.FOOD,
                BigDecimal.valueOf(-2),
                Category.HEALTH,
                BigDecimal.valueOf(20),
                Category.SHOPPING,
                BigDecimal.valueOf(-12)
            ),
            result
        );
    }

    @Test
    @DisplayName("filterByCategory should throw on null transactions")
    void testFilterByCategoryWithNullList() {
        assertThrows(NullPointerException.class, () ->
            service.filterByCategory(null, Category.SHOPPING)
        );
    }

    @Test
    @DisplayName("filterByCategory should return empty list on null category")
    void testFilterByCategoryWithNullCategory() {
        var result = service.filterByCategory(transactions, null);
        assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("sumByCategory should throw on null transactions")
    void testSumByCategoryWithNullList() {
        assertThrows(NullPointerException.class, () ->
            service.sumByCategory(null)
        );
    }

    @Test
    @DisplayName("sumByCategory should throw on transaction with null category")
    void testSumByCategoryWithTransactionWithNullCategory() {
        var txns = new ArrayList<>(transactions);
        txns.add(
            new Transaction(LocalDate.now(), "f", BigDecimal.valueOf(100), null)
        );
        assertThrows(NullPointerException.class, () ->
            service.sumByCategory(txns)
        );
    }

    @Test
    @DisplayName("calculateTotal should throw on null in list")
    void testCalculateTotalWithNullInList() {
        var txns = new ArrayList<Transaction>(transactions);
        txns.add(null);
        assertThrows(NullPointerException.class, () ->
            service.calculateTotal(txns)
        );
    }

    @Test
    @DisplayName("filterByCategory should throw on null in list")
    void testFilterByCategoryWithNullInList() {
        var txns = new ArrayList<Transaction>(transactions);
        txns.add(null);
        assertThrows(NullPointerException.class, () ->
            service.filterByCategory(txns, Category.SHOPPING)
        );
    }

    @Test
    @DisplayName("sumByCategory should throw on null in list")
    void testSumByCategoryWithNullInList() {
        var txns = new ArrayList<Transaction>(transactions);
        txns.add(null);
        assertThrows(NullPointerException.class, () ->
            service.sumByCategory(txns)
        );
    }
}
