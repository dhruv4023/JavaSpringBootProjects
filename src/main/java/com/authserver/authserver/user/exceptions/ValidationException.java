package com.authserver.authserver.user.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}