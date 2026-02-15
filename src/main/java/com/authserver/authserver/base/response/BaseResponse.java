package com.authserver.authserver.base.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.authserver.authserver.base.helper.PageResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    public BaseResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    private boolean success;
    private String message;
    private List<T> data;
    private PageResponse page;
}
