package com.kofta.app.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kofta.app.user.User;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryAccountRepositoryTest {

    private InMemoryAccountRepository repository;
    private Account account1;
    private Account account2;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        user1 = new User(UUID.randomUUID(), "user1");
        user2 = new User(UUID.randomUUID(), "user2");
        account1 = new Account(
            UUID.randomUUID(),
            "account1",
            "USD",
            user1.getId()
        );
        account2 = new Account(
            UUID.randomUUID(),
            "account2",
            "EUR",
            user2.getId()
        );
        repository.save(account1);
        repository.save(account2);
    }

    @Test
    @DisplayName("Should find an account by its ID")
    void testFindById() {
        var result = repository.findById(account1.getId());
        assertTrue(result.isPresent());
        assertEquals(account1, result.get());
    }

    @Test
    @DisplayName("Should return empty optional for non-existent ID")
    void testFindByIdNotFound() {
        var result = repository.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should find accounts by user ID")
    void testFindByUserId() {
        var result = repository.findByUserId(user1.getId());
        assertEquals(1, result.size());
        assertEquals(account1, result.get(0));
    }

    @Test
    @DisplayName("Should return empty list for user with no accounts")
    void testFindByUserIdNotFound() {
        var result = repository.findByUserId(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }
}
