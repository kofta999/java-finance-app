package com.kofta.app.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
        user1 = new User(UUID.randomUUID(), "user1");
        user2 = new User(UUID.randomUUID(), "user2");
        repository.save(user1);
        repository.save(user2);
    }

    @Test
    @DisplayName("Should find a user by their ID")
    void testFindById() {
        var result = repository.findById(user1.getId());
        assertTrue(result.isPresent());
        assertEquals(user1, result.get());
    }

    @Test
    @DisplayName("Should return empty optional for non-existent ID")
    void testFindByIdNotFound() {
        var result = repository.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }
}
