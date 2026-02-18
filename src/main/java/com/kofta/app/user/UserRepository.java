package com.kofta.app.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID userId);
    List<User> findAll();
    void save(User user);
}
