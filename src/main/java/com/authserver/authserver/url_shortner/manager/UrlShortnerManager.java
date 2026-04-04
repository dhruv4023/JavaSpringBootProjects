package com.authserver.authserver.url_shortner.manager;

import com.authserver.authserver.redis.RedisCacheService;
import com.authserver.authserver.url_shortner.repository.UrlShortnerRepository;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

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

    public UrlShortnerManager(
            UrlShortnerRepository repository, RedisCacheService redisCacheService) {
        this.repository = repository;
        this.redisCacheService = redisCacheService;
    }

    public String add(UrlShortnerEntry entry) {

        String normalizedUrl = normalize(entry.getOriginalUrl());

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

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int LENGTH = 6;
    private static final Random RANDOM = new Random();

    public String generateShortCode() {
        StringBuilder sb = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            int index = RANDOM.nextInt(BASE62.length());
            sb.append(BASE62.charAt(index));
        }

        return sb.toString();
    }

    public String generateUniqueShortCode() {
        String code;

        do {
            code = generateShortCode();
        } while (repository.existsByShortCode(code));

        return code;
    }

    public String normalize(String url) {
        try {
            URI uri = new URI(url);

            String scheme = uri.getScheme().toLowerCase();
            String host = uri.getHost().toLowerCase();
            String path = uri.getPath();

            // Parse query params
            String query = uri.getQuery();
            Map<String, String> params = new TreeMap<>();

            if (query != null) {
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");

                    String key = pair[0];

                    if (key.startsWith("utm_") ||
                            key.equals("sxsrf") ||
                            key.equals("ved") ||
                            key.equals("ei")) {
                        continue;
                    }

                    String value = pair.length > 1 ? pair[1] : "";
                    params.put(key, value);
                }
            }

            // Rebuild query
            StringBuilder normalizedQuery = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (normalizedQuery.length() > 0)
                    normalizedQuery.append("&");
                normalizedQuery.append(entry.getKey()).append("=").append(entry.getValue());
            }

            return scheme + "://" + host +
                    (path != null ? path : "") +
                    (normalizedQuery.length() > 0 ? "?" + normalizedQuery : "");

        } catch (URISyntaxException e) {
            return url;
        }
    }
}
