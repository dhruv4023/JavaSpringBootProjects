package com.authserver.authserver.user.exceptions;

import org.springframework.http.HttpStatus;

import com.authserver.authserver.base.exception.BaseApiException;

public class UserNotFoundException extends BaseApiException {
    public UserNotFoundException() {
        super("user not found", HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }
}