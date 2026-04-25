package com.authserver.authserver.base.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.authserver.authserver.base.helper.PageResponse;

@Data
@NoArgsConstructor
public class BaseResponse<T> {

    private boolean success;
    private String message;
    private String errorCode;
    private T data;
    private PageResponse page;

    public static <T> BaseResponse<T> success(String message, T data) {
        BaseResponse<T> res = new BaseResponse<>();
        res.success = true;
        res.message = message;
        res.data = data;
        return res;
    }

    public static <T> BaseResponse<T> success(String message, T data, PageResponse page) {
        BaseResponse<T> res = new BaseResponse<>();
        res.success = true;
        res.message = message;
        res.data = data;
        res.page = page;
        return res;
    }

    public static <T> BaseResponse<T> failure(String message, String errorCode) {
        BaseResponse<T> res = new BaseResponse<>();
        res.success = false;
        res.message = message;
        res.errorCode = errorCode;
        return res;
    }
}