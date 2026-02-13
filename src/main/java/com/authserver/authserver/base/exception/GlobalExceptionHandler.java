package com.authserver.authserver.base.exception;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.authserver.authserver.base.response.BaseResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseApiException.class)
    public ResponseEntity<BaseResponse<Object>> handleBaseApiException(BaseApiException ex) {
        return ResponseEntity
                .status(Objects.requireNonNull(ex.getStatus()))
                .body(new BaseResponse<>(false, ex.getMessage(), null, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGenericException(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse<>(false, "Internal server error: " + ex.getMessage(), null, null));
    }
}
