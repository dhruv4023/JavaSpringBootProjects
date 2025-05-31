package com.authserver.authserver.user.exceptions;

public
class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}