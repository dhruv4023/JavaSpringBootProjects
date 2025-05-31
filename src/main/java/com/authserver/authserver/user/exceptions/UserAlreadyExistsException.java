package com.authserver.authserver.user.exceptions;

public
class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}