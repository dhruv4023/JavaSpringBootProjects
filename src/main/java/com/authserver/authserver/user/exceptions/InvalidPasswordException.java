package com.authserver.authserver.user.exceptions;

public
class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}