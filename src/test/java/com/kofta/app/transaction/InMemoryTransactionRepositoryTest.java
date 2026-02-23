package com.kofta.app.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryTransactionRepositoryTest {

    private InMemoryTransactionRepository repository;
    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTransactionRepository();
        transaction1 = new Transaction(
            UUID.randomUUID(),
            LocalDate.now(),
            "transaction 1",
            BigDecimal.TEN,
            Category.FOOD,
            UUID.randomUUID()
        );
        transaction2 = new Transaction(
            UUID.randomUUID(),
            LocalDate.now(),
            "transaction 2",
            BigDecimal.ONE,
            Category.SHOPPING,
            UUID.randomUUID()
        );
        repository.save(transaction1);
        repository.save(transaction2);
    }

    @Test
    @DisplayName("Should find a transaction by its ID")
    void testFindById() {
        var result = repository.findById(transaction1.id());
        assertTrue(result.isPresent());
        assertEquals(transaction1, result.get());
    }

    @Test
    @DisplayName("Should return empty optional for non-existent ID")
    void testFindByIdNotFound() {
        var result = repository.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should find all transactions")
    void testFindAll() {
        var result = repository.findAll();
        assertEquals(2, result.size());
        assertTrue(result.contains(transaction1));
        assertTrue(result.contains(transaction2));
    }

    @Test
    @DisplayName("Should find transactions by category")
    void testFindByCategory() {
        var filter = new TransactionFilter(null, Category.SHOPPING);
        var result = repository.findAll(filter);
        assertEquals(1, result.size());
        assertEquals(transaction2, result.get(0));
    }

    @Test
    @DisplayName("Should return empty list when no transactions match category")
    void testFindByCategoryNotFound() {
        var filter = new TransactionFilter(null, Category.HEALTH);
        var result = repository.findAll(filter);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should find transactions by accountId")
    void testFindByAccountId() {
        var filter = new TransactionFilter(transaction1.accountId(), null);
        var result = repository.findAll(filter);
        assertEquals(1, result.size());
        assertEquals(transaction1, result.get(0));
    }

    @Test
    @DisplayName("Should delete a transaction by its ID")
    void testDeleteById() {
        repository.deleteById(transaction1.id());
        var result = repository.findById(transaction1.id());
        assertTrue(result.isEmpty());
        assertEquals(1, repository.findAll().size());
    }

    @Test
    @DisplayName("Should handle deleting a non-existent transaction gracefully")
    void testDeleteByIdNotFound() {
        repository.deleteById(UUID.randomUUID());
        assertEquals(2, repository.findAll().size());
    }

    @Test
    @DisplayName("findAll should return empty list for new repository")
    void testFindAllEmpty() {
        var newRepo = new InMemoryTransactionRepository();
        assertTrue(newRepo.findAll().isEmpty());
    }
}
