package com.kofta.app.common.result;

public sealed interface Result<T, E extends Throwable> {
    record Ok<T, E extends Throwable>(T value) implements Result<T, E> {}

    record Err<T, E extends Throwable>(E error) implements Result<T, E> {}
}
