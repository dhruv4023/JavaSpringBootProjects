package com.authserver.authserver.base.service;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.base.helper.PageResponse;
import com.authserver.authserver.base.response.BaseResponse;

import java.util.Collections;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class BaseService<Entry, ID> implements BaseServiceInterface<Entry, ID> {

    private final BaseManager<?, ID, Entry> manager;

    public BaseService(BaseManager<?, ID, Entry> manager) {
        this.manager = manager;
    }

    protected ResponseEntity<BaseResponse<Entry>> handleServiceOperation(
            Supplier<Entry> operation,
            String successMessage,
            HttpStatus successStatus) {

        Entry result = operation.get();
        return ResponseEntity.status(Objects.nonNull(successStatus) ? successStatus : HttpStatus.OK).body(
                new BaseResponse<>(true, successMessage, Collections.singletonList(result), null));
    }

    public ResponseEntity<BaseResponse<Entry>> add(Entry entry) {
        return handleServiceOperation(() -> manager.add(entry), "Added successfully", HttpStatus.CREATED);
    }

    public ResponseEntity<BaseResponse<Entry>> update(ID id, Entry entry) {
        return handleServiceOperation(() -> manager.update(id, entry), "Updated successfully", null);
    }

    public ResponseEntity<Void> delete(ID id) {
        manager.delete(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<BaseResponse<Entry>> get(ID id) {
        return handleServiceOperation(() -> manager.getById(id), "Fetched successfully", null);
    }

    public ResponseEntity<BaseResponse<Entry>> getAll(long page, long size) {
        Page<Entry> result = manager.get(page, size);
        PageResponse pageResponse = new PageResponse(page, size, result.getTotalElements());
        return ResponseEntity.ok(new BaseResponse<>(true, "Fetched successfully", result.getContent(), pageResponse));
    }
}
