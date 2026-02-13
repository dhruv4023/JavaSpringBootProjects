package com.authserver.authserver.base.service;

import org.springframework.http.ResponseEntity;

import com.authserver.authserver.base.response.BaseResponse;

public interface BaseServiceInterface<Entry, ID> {

    ResponseEntity<BaseResponse<Entry>> add(Entry entry);

    ResponseEntity<BaseResponse<Entry>> update(ID id, Entry entry);

    ResponseEntity<Void> delete(ID id);

    ResponseEntity<BaseResponse<Entry>> get(ID id);

    ResponseEntity<BaseResponse<Entry>> getAll(long page, long size);
}
