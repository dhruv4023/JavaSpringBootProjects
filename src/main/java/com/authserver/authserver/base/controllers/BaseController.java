package com.authserver.authserver.base.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.base.service.BaseService;


public abstract class BaseController<ID, Entry, Service extends BaseService<ID, Entry, ?>> implements BaseControllerInterface<Entry, ID> {

    @Autowired
    protected Service service;

    @PostMapping("/add")
    public ResponseEntity<BaseResponse<Entry>> add(@RequestBody Entry entry) {
        return service.add(entry);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<Entry>> update(@PathVariable ID id, @RequestBody Entry entry) {
        return service.update(id, entry);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        return service.delete(id);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<BaseResponse<Entry>> getById(@PathVariable ID id) {
        return service.get(id);
    }

    @GetMapping("/getAll")
    public ResponseEntity<BaseResponse<Entry>> getAll(@RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        return service.getAll(page, size);
    }
}
