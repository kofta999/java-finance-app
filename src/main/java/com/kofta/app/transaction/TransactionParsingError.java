package com.kofta.app.transaction;

public class TransactionParsingError extends RuntimeException {

    public TransactionParsingError(String message, Throwable cause) {
        super(message, cause);
    }
}
