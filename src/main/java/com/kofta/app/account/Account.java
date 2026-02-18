package com.kofta.app.account;

import java.util.Objects;
import java.util.UUID;

public class Account {

    UUID id;
    String name;
    String currency;
    UUID userId;

    public Account(UUID id, String name, String currency, UUID userId) {
        this.id = id;
        this.name = name;
        this.currency = currency;
        this.userId = userId;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Account a && Objects.equals(a.getId(), this.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
