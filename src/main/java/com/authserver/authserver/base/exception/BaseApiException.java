package com.authserver.authserver.base.exception;

import java.util.Objects;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;

public abstract class BaseApiException extends RuntimeException {

    @Getter
    private final HttpStatusCode status;
    
    @Getter
    private final String errorCode;

    protected BaseApiException(String message, HttpStatusCode status, String errorCode) {
        super(message);
        this.status = Objects.requireNonNull(status);
        this.errorCode = errorCode;
    }
}
