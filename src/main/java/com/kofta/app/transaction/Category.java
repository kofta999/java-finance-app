package com.kofta.app.transaction;

import java.util.Optional;

public enum Category {
    SALARY,
    FOOD,
    RENT,
    SHOPPING,
    HEALTH;

    public static Optional<Category> fromString(String input) {
        try {
            return Optional.of(Category.valueOf(input.toUpperCase().trim()));
        } catch (IllegalArgumentException | NullPointerException e) {
            return Optional.empty();
        }
    }
}
