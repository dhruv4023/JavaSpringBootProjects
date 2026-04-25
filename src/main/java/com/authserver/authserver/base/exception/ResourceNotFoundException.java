package com.authserver.authserver.base.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseApiException {

    public ResourceNotFoundException(String resourceName) {
        super(resourceName + " not found", HttpStatus.NOT_FOUND, resourceName.toUpperCase() + "_NOT_FOUND");
    }
}
