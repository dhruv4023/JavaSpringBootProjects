package com.authserver.authserver.url_shortner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.url_shortner.entry.UrlShortnerEntry;
import com.authserver.authserver.url_shortner.service.UrlShortnerService;

@RestController
public class UrlShortnerController {
    
    @Autowired
    protected UrlShortnerService service;


    @PostMapping("/url-shortner/add")
    public ResponseEntity<BaseResponse<String>> add(@RequestBody UrlShortnerEntry entry) {
        return service.add(entry);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> getById(@PathVariable String shortCode) {
        return service.get(shortCode);
    }
}
