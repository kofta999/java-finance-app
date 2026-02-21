package com.kofta.app.common.result;

public sealed interface Result<T, E extends Throwable> {
    boolean isOk();
    boolean isErr();
    T unwrap() throws E;
    T unwrapOrElse(T other);

    record Ok<T, E extends Throwable>(T value) implements Result<T, E> {
        @Override
        public boolean isOk() {
            return true;
        }

        @Override
        public boolean isErr() {
            return false;
        }

        @Override
        public T unwrap() throws E {
            return this.value();
        }

        @Override
        public T unwrapOrElse(T other) {
            return this.value();
        }
    }

    record Err<T, E extends Throwable>(E error) implements Result<T, E> {
        @Override
        public boolean isOk() {
            return false;
        }

        @Override
        public boolean isErr() {
            return true;
        }

        @Override
        public T unwrap() throws E {
            throw error;
        }

        @Override
        public T unwrapOrElse(T other) {
            return other;
        }
    }
}
