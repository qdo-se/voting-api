package com.qdo.votingapi.exceptions;

public class ApiTransactionException extends RuntimeException {
    public ApiTransactionException(String message) {
        super(message);
    }
}
