package com.authserver.authserver.base.service;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.base.helper.PageResponse;
import com.authserver.authserver.base.response.BaseResponse;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class BaseService<ID, Entry, Manager extends BaseManager<ID, Entry, ?, ?>>
        implements BaseServiceInterface<Entry, ID> {

    protected final Manager manager;

    public BaseService(Manager manager) {
        this.manager = manager;
    }

    protected ResponseEntity<BaseResponse<Entry>> handleServiceOperation(
            Supplier<List<Entry>> operation,
            String successMessage,
            HttpStatus successStatus,
            PageResponse pageResponse) {

        List<Entry> result = operation.get();
        return ResponseEntity.status(Objects.nonNull(successStatus) ? successStatus : HttpStatus.OK).body(
                new BaseResponse<>(true, successMessage, (result), pageResponse));
    }    

    public ResponseEntity<BaseResponse<Entry>> add(Entry entry) {
        return handleServiceOperation(() -> Collections.singletonList(manager.add(entry)), "Added successfully",
                HttpStatus.CREATED, null);
    }

    public ResponseEntity<BaseResponse<Entry>> update(ID id, Entry entry) {
        return handleServiceOperation(() -> Collections.singletonList(manager.update(id, entry)),
                "Updated successfully", null, null);
    }

    public ResponseEntity<Void> delete(ID id) {
        manager.delete(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<BaseResponse<Entry>> get(ID id) {
        return handleServiceOperation(() -> Collections.singletonList(manager.getById(id)), "Fetched successfully",
                null, null);
    }

    public ResponseEntity<BaseResponse<Entry>> getAll(long page, long size) {
        Page<Entry> result = manager.get(page, size);
        PageResponse pageResponse = new PageResponse(page, size, result.getTotalElements());
        return handleServiceOperation(()-> result.getContent(), "Fetched successfully", null, pageResponse);
    }
}
