package com.authserver.authserver.base.response;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.authserver.authserver.base.helper.PageResponse;

public final class ResponseBuilder {

    public static <T> ResponseEntity<BaseResponse<T>> single(
            Supplier<T> operation,
            String message,
            HttpStatus status) {

        T result = Objects.isNull(operation) ? null : operation.get();

        return ResponseEntity
                .status(status != null ? status : HttpStatus.OK)
                .body(BaseResponse.success(message, result));
    }

    public static <T> ResponseEntity<BaseResponse<List<T>>> list(
            Supplier<List<T>> operation,
            String message,
            HttpStatus status,
            PageResponse page) {

        List<T> result = Objects.isNull(operation) ? null : operation.get();

        return ResponseEntity
                .status(status != null ? status : HttpStatus.OK)
                .body(BaseResponse.success(message, result, page));
    }
}
