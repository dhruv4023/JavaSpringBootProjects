package com.authserver.authserver.user.exceptions;

import org.springframework.http.HttpStatus;

import com.authserver.authserver.base.exception.BaseApiException;

public class InvalidPasswordException extends BaseApiException {
    public InvalidPasswordException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "INVALID_PASSWORD");
    }
}