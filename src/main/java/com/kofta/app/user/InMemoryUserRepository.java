package com.kofta.app.user;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class InMemoryUserRepository implements UserRepository {

    private HashMap<UUID, User> map;

    public InMemoryUserRepository() {
        this.map = new HashMap<>();
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(map.get(userId));
    }

    @Override
    public void save(User user) {
        map.put(user.getId(), user);
    }
}
