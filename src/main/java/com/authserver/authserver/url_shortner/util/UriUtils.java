package com.authserver.authserver.url_shortner.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

@Component
public class UriUtils {

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int LENGTH = 6;
    private static final Random RANDOM = new Random();
    private static final List<String> ALLOWED_DOMAINS = List.of(
            "google.com",
            "github.com");

    public String generateShortCode() {
        StringBuilder sb = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            int index = RANDOM.nextInt(BASE62.length());
            sb.append(BASE62.charAt(index));
        }

        return sb.toString();
    }

    public String normalize(String url) {
        try {
            URI uri = new URI(url);
            isValidUrl(uri);
            isAllowedDomain(uri);

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

    private void isValidUrl(URI uri) {
        try {
            if (uri.getScheme() != null &&
                    (uri.getScheme().equalsIgnoreCase("http") ||
                            uri.getScheme().equalsIgnoreCase("https"))) {
                return;
            }

            throw new IllegalArgumentException("Invalid URL");
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL");
        }
    }

    private void isAllowedDomain(URI uri) {
        try {
            String host = uri.getHost();

            if (host == null)
                throw new IllegalArgumentException("Invalid URL host");

            if (ALLOWED_DOMAINS.stream().anyMatch(domain -> host.equals(domain) || host.endsWith("." + domain)))
                return;

            throw new IllegalArgumentException("Not Allowed Domain");
        } catch (Exception e) {
            throw new IllegalArgumentException("Not Allowed Domain");
        }
    }
}
