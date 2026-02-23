package com.kofta.app.user;

import java.util.List;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User create(UserDto dto) {
        var user = new User(UUID.randomUUID(), dto.name());
        // TODO: Proper error handling
        this.userRepository.save(user).unwrap();

        return user;
    }
}
