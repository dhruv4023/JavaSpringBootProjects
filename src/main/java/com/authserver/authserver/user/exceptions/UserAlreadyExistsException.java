package com.authserver.authserver.user.exceptions;

import org.springframework.http.HttpStatus;

import com.authserver.authserver.base.exception.BaseApiException;

public class UserAlreadyExistsException extends BaseApiException {
    public UserAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}