package com.authserver.authserver.base.exception;

import java.util.Objects;

import org.springframework.http.HttpStatusCode;

public abstract class BaseApiException extends RuntimeException {

    private final HttpStatusCode status;

    protected BaseApiException(String message, HttpStatusCode status) {
        super(message);
        this.status = Objects.requireNonNull(status);
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}
