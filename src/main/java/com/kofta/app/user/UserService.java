package com.kofta.app.user;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User create(UserDto dto);
}
