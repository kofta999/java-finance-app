package com.kofta.app.finance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.Transaction;
import com.kofta.app.transaction.TransactionRepository;
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

    @InjectMocks
    private FinanceServiceImpl service;

    private UUID accountId;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
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
}
