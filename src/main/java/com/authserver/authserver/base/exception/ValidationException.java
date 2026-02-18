package com.authserver.authserver.base.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends BaseApiException {
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}