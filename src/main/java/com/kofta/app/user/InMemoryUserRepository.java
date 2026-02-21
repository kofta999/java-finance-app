package com.kofta.app.user;

import com.kofta.app.common.repository.EntityNotFoundError;
import com.kofta.app.common.result.Result;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public List<User> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public Result<Void, EntityNotFoundError> save(User user) {
        map.put(user.getId(), user);
        return new Result.Ok<>(null);
    }
}
