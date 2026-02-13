package com.authserver.authserver.base.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.base.service.BaseService;

public abstract class BaseController<Entry, ID> implements BaseControllerInterface<Entry, ID> {

    protected abstract BaseService<Entry, ID> getService();

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<Entry>> add(@RequestBody Entry entry) {
        return getService().add(entry);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<Entry>> update(@PathVariable ID id, @RequestBody Entry entry) {
        return getService().update(id, entry);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        return getService().delete(id);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<Entry>> getById(@PathVariable ID id) {
        return getService().get(id);
    }

    @GetMapping("/getAll")
    public ResponseEntity<BaseResponse<Entry>> getAll(@RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return getService().getAll(page, size);
    }
}
