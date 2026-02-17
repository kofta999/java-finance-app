package com.kofta.app.transaction;

public class TransactionParsingException extends RuntimeException {

    public TransactionParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
