package com.authserver.authserver.base.service;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.base.helper.PageResponse;
import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.base.response.ResponseBuilder;

import java.util.List;

public abstract class BaseService<ID, Entry, Manager extends BaseManager<ID, Entry, ?, ?>>
        implements BaseServiceInterface<Entry, ID> {

    protected final Manager manager;

    public BaseService(Manager manager) {
        this.manager = manager;
    }

    public ResponseEntity<BaseResponse<Entry>> add(Entry entry) {
        return ResponseBuilder.single(() -> (manager.add(entry)), "Added successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<BaseResponse<Entry>> update(ID id, Entry entry) {
        return ResponseBuilder.single(() -> (manager.update(id, entry)), "Updated successfully", null);
    }

    public ResponseEntity<Void> delete(ID id) {
        manager.delete(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<BaseResponse<Entry>> get(ID id) {
        return ResponseBuilder.single(() -> (manager.getById(id)), "Fetched successfully", null);
    }

    public ResponseEntity<BaseResponse<List<Entry>>> getAll(long page, long size) {
        Page<Entry> result = manager.get(page, size);
        PageResponse pageResponse = new PageResponse(page, size, result.getTotalElements());
        return ResponseBuilder.list(() -> result.getContent(), "Fetched successfully", null, pageResponse);
    }
}
