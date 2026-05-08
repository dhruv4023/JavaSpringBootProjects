package com.authserver.authserver.expense_tracker.exceptions;

import org.springframework.http.HttpStatus;

import com.authserver.authserver.base.exception.BaseApiException;

public class LabelDeleteException extends BaseApiException {

    public LabelDeleteException(String message) {
        super("Cannot delete label with existing "+ message, HttpStatus.CONFLICT, "LABEL_DELETE_CONFLICT");
    }
}
