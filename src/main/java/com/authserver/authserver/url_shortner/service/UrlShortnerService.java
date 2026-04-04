package com.authserver.authserver.url_shortner.service;

import java.net.URI;
import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.url_shortner.entry.UrlShortnerEntry;
import com.authserver.authserver.url_shortner.manager.UrlShortnerManager;

@Component
public class UrlShortnerService {

    protected final UrlShortnerManager manager;

    public UrlShortnerService(UrlShortnerManager manager) {
        this.manager = manager;
    }

    public ResponseEntity<BaseResponse<String>> add(UrlShortnerEntry entry) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse<>(true, "short URL created", Collections.singletonList(manager.add(entry)), null));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> get(@PathVariable String shortCode) {
        String url = manager.get(shortCode);

        if (url == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url))
                .build();
    }
}
