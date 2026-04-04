package com.authserver.authserver.url_shortner.manager;

import com.authserver.authserver.redis.RedisCacheService;
import com.authserver.authserver.url_shortner.repository.UrlShortnerRepository;
import com.authserver.authserver.url_shortner.util.UriUtils;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.authserver.authserver.url_shortner.entry.UrlShortnerEntry;
import com.authserver.authserver.url_shortner.model.UrlShortner;

@Component
@Slf4j
public class UrlShortnerManager {

    private final RedisCacheService redisCacheService;
    private final UrlShortnerRepository repository;
    private final UriUtils utils;


    public UrlShortnerManager( UriUtils utils,
            UrlShortnerRepository repository, RedisCacheService redisCacheService) {
        this.repository = repository;
        this.redisCacheService = redisCacheService;
        this.utils = utils;
    }

    public String add(UrlShortnerEntry entry) {

        String normalizedUrl = utils.normalize(entry.getOriginalUrl());

        UrlShortner model = UrlShortner.builder()
                .originalUrl(normalizedUrl)
                .shortCode(generateUniqueShortCode())
                .clickCount(0L)
                .expiryAt(null)
                .build();

        try {
            UrlShortner saved = repository.save(model);
            redisCacheService.set(saved.getShortCode(), normalizedUrl, Duration.ofHours(24));
            return buildShortUrl(saved.getShortCode());

        } catch (DataIntegrityViolationException ex) {
            // duplicate originalUrl → fetch existing
            UrlShortner existing = repository.findByOriginalUrl(normalizedUrl)
                    .orElseThrow(() -> new RuntimeException("Unexpected error"));

            return buildShortUrl(existing.getShortCode());
        }
    }

    private String buildShortUrl(String shortCode) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{shortCode}")
                .buildAndExpand(shortCode)
                .toUriString();
    }

    public String get(String shortCode) {
        String url = redisCacheService.get(shortCode, String.class);
        if (url != null) {
            redisCacheService.set(shortCode, url, Duration.ofHours(24));
            return url;
        }

        UrlShortner model = repository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));

        if (model.getExpiryAt() != null &&
                model.getExpiryAt() < System.currentTimeMillis()) {
            throw new RuntimeException("Short URL expired");
        }

        url = model.getOriginalUrl();
        redisCacheService.set(shortCode, url, Duration.ofHours(24));
        model.setClickCount(model.getClickCount() + 1);
        repository.save(model);

        return model.getOriginalUrl();
    }

    public String generateUniqueShortCode() {
        String code;

        do {
            code = utils.generateShortCode();
        } while (repository.existsByShortCode(code));

        return code;
    }

}
