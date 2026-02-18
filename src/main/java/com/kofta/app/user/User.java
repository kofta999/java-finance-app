package com.kofta.app.user;

import java.util.Objects;
import java.util.UUID;

public class User {

    UUID id;
    String name;

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User u && Objects.equals(u.getId(), this.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
