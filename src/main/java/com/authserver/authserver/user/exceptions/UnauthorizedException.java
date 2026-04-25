package com.authserver.authserver.user.exceptions;

import org.springframework.http.HttpStatus;

import com.authserver.authserver.base.exception.BaseApiException;

public class UnauthorizedException extends BaseApiException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
}
