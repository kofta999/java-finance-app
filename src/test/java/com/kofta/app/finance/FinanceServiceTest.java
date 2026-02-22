package com.kofta.app.finance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kofta.app.common.result.Result;
import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.ParsedTransaction;
import com.kofta.app.transaction.Transaction;
import com.kofta.app.transaction.TransactionParser;
import com.kofta.app.transaction.TransactionParsingError;
import com.kofta.app.transaction.TransactionRepository;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionParser transactionParser;

    @InjectMocks
    private FinanceServiceImpl service;

    private UUID accountId;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Should initialize transactions from CSV")
    void testInitializeFromCsv() {
        var stream = new ByteArrayInputStream("".getBytes());
        var parsedTransactions = List.of(
            new ParsedTransaction(
                LocalDate.now(),
                "desc",
                BigDecimal.TEN,
                Category.FOOD
            )
        );
        when(transactionParser.from(any(InputStream.class))).thenReturn(
            new Result.Ok<>(parsedTransactions)
        );

        service.initializeFromCsv(stream, accountId);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw exception on parsing error")
    void testInitializeFromCsvError() {
        var stream = new ByteArrayInputStream("".getBytes());
        var error = new TransactionParsingError(
            "Error",
            new RuntimeException()
        );
        when(transactionParser.from(any(InputStream.class))).thenReturn(
            new Result.Err<>(error)
        );

        assertThrows(TransactionParsingError.class, () ->
            service.initializeFromCsv(stream, accountId)
        );
    }

    @Test
    @DisplayName("Total should be 0 when list is empty")
    void testEmptyList() {
        when(transactionRepository.findAll()).thenReturn(
            Collections.emptyList()
        );
        var result = service.calculateTotal(accountId);
        assertEquals(BigDecimal.ZERO, result, "Total of empty list must be 0");
    }

    @Test
    @DisplayName("Should calculate results correctly")
    void testList() {
        var transactions = List.of(
            new Transaction(
                UUID.randomUUID(),
                LocalDate.now(),
                "a",
                new BigDecimal("10"),
                Category.FOOD,
                accountId
            ),
            new Transaction(
                UUID.randomUUID(),
                LocalDate.now(),
                "b",
                new BigDecimal("-12"),
                Category.FOOD,
                accountId
            )
        );
        when(transactionRepository.findAll()).thenReturn(transactions);
        var result = service.calculateTotal(accountId);
        assertEquals(new BigDecimal("-2"), result);
    }

    @Test
    @DisplayName("Get all shopping transactions")
    void testShoppingCategory() {
        var transactions = List.of(
            new Transaction(
                UUID.randomUUID(),
                LocalDate.now(),
                "a",
                new BigDecimal("10"),
                Category.FOOD,
                accountId
            ),
            new Transaction(
                UUID.randomUUID(),
                LocalDate.now(),
                "d",
                new BigDecimal("-30"),
                Category.SHOPPING,
                accountId
            ),
            new Transaction(
                UUID.randomUUID(),
                LocalDate.now(),
                "e",
                new BigDecimal("18"),
                Category.SHOPPING,
                accountId
            )
        );

        when(transactionRepository.findAll(any(Predicate.class))).thenAnswer(
            invocation -> {
                Predicate<Transaction> predicate = invocation.getArgument(0);
                return transactions
                    .stream()
                    .filter(predicate)
                    .collect(Collectors.toList());
            }
        );

        var result = service.filterByCategory(accountId, Category.SHOPPING);
        assertEquals(2, result.size());
        assertEquals(Category.SHOPPING, result.get(0).category());
        assertEquals(Category.SHOPPING, result.get(1).category());
    }

    @Test
    @DisplayName("Use a category not in list, should get empty list")
    void testNonExistentCategory() {
        var transactions = List.of(
            new Transaction(
                UUID.randomUUID(),
                LocalDate.now(),
                "a",
                new BigDecimal("10"),
                Category.FOOD,
                accountId
            )
        );
        when(transactionRepository.findAll(any(Predicate.class))).thenAnswer(
            invocation -> {
                Predicate<Transaction> predicate = invocation.getArgument(0);
                return transactions
                    .stream()
                    .filter(predicate)
                    .collect(Collectors.toList());
            }
        );

        var result = service.filterByCategory(accountId, Category.RENT);
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    @DisplayName("Sum by category")
    void testSumByCategory() {
        var transactions = List.of(
            new Transaction(
                UUID.randomUUID(),
                LocalDate.now(),
                "a",
                new BigDecimal("10"),
                Category.FOOD,
                accountId
            ),
            new Transaction(
                UUID.randomUUID(),
                LocalDate.now(),
                "b",
                new BigDecimal("-12"),
                Category.FOOD,
                accountId
            ),
            new Transaction(
                UUID.randomUUID(),
                LocalDate.now(),
                "c",
                new BigDecimal("20"),
                Category.HEALTH,
                accountId
            ),
            new Transaction(
                UUID.randomUUID(),
                LocalDate.now(),
                "d",
                new BigDecimal("-30"),
                Category.SHOPPING,
                accountId
            ),
            new Transaction(
                UUID.randomUUID(),
                LocalDate.now(),
                "e",
                new BigDecimal("18"),
                Category.SHOPPING,
                accountId
            )
        );
        when(transactionRepository.findAll(any(Predicate.class))).thenAnswer(
            invocation -> {
                Predicate<Transaction> predicate = invocation.getArgument(0);
                return transactions
                    .stream()
                    .filter(predicate)
                    .collect(Collectors.toList());
            }
        );
        var result = service.sumByCategory(accountId);
        assertEquals(
            Map.of(
                Category.FOOD,
                new BigDecimal("-2"),
                Category.HEALTH,
                new BigDecimal("20"),
                Category.SHOPPING,
                new BigDecimal("-12")
            ),
            result
        );
    }

    @Test
    @DisplayName("Sort transactions")
    void testSortTransactions() {
        var t1 = new Transaction(
            UUID.randomUUID(),
            LocalDate.of(2023, 1, 1),
            "a",
            new BigDecimal("100"),
            Category.FOOD,
            accountId
        );
        var t2 = new Transaction(
            UUID.randomUUID(),
            LocalDate.of(2023, 1, 2),
            "b",
            new BigDecimal("50"),
            Category.RENT,
            accountId
        );
        var t3 = new Transaction(
            UUID.randomUUID(),
            LocalDate.of(2023, 1, 3),
            "c",
            new BigDecimal("200"),
            Category.HEALTH,
            accountId
        );

        var transactions = List.of(t2, t3, t1); // Unsorted

        when(transactionRepository.findAll(any(Predicate.class))).thenAnswer(
            invocation -> {
                Predicate<Transaction> predicate = invocation.getArgument(0);
                return transactions
                    .stream()
                    .filter(predicate)
                    .collect(Collectors.toList());
            }
        );

        // Sort by Amount
        // 50, 100, 200 -> t2, t1, t3
        var sortedByAmount = service.sortTransactionsBy(
            accountId,
            TransactionSort.AMOUNT
        );
        assertEquals(List.of(t2, t1, t3), sortedByAmount);

        // Sort by Date
        // 2023-01-01, 2023-01-02, 2023-01-03 -> t1, t2, t3
        var sortedByDate = service.sortTransactionsBy(
            accountId,
            TransactionSort.DATE
        );
        assertEquals(List.of(t1, t2, t3), sortedByDate);

        // Sort by Category
        // FOOD (0), HEALTH (1), RENT (2),  -> t1, t3, t2
        var sortedByCategory = service.sortTransactionsBy(
            accountId,
            TransactionSort.CATEGORY
        );
        assertEquals(List.of(t1, t3, t2), sortedByCategory);
    }
}
