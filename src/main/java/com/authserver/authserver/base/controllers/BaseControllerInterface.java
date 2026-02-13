package com.authserver.authserver.base.controllers;

import org.springframework.http.ResponseEntity;

import com.authserver.authserver.base.response.BaseResponse;

public interface BaseControllerInterface<Entry, ID> {
    public ResponseEntity<BaseResponse<Entry>> add(Entry entry);
    public ResponseEntity<BaseResponse<Entry>> getById(ID id);
    public ResponseEntity<BaseResponse<Entry>> update(ID id, Entry entry);
    public ResponseEntity<Void> delete(ID id);
    public ResponseEntity<BaseResponse<Entry>> getAll(long page, long size);
}
