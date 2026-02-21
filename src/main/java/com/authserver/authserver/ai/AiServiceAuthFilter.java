package com.authserver.authserver.ai;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AiServiceAuthFilter extends OncePerRequestFilter {

    @Value("${ai.service.key}")
    private String aiServiceKey;

    @Value("${ai.service.secret}")
    private String aiSecret;

    private static final long MAX_REQUEST_AGE_MS = 30000; // 30 seconds

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI().startsWith("/api") ? request.getRequestURI()
                .substring(4) : request.getRequestURI();

        if (!requestURI.startsWith("/ai")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ===== Content Type Validation =====
        String contentType = request.getContentType();
        if (contentType == null || !contentType.contains("application/json")) {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        // ===== API Key Validation =====
        String apiKey = request.getHeader("X-AI-Service-Key");
        if (apiKey == null || !apiKey.equals(aiServiceKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // ===== Timestamp Validation =====
        String timestamp = request.getHeader("X-Request-Time");

        if (timestamp == null || !isValidTimestamp(timestamp)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // ===== Signature Validation =====
        String signature = request.getHeader("X-AI-Signature");

        if (signature == null || !validateSignature(request, signature)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidTimestamp(String timestamp) {
        try {
            long reqTime = Long.parseLong(timestamp);
            long diff = System.currentTimeMillis() - reqTime;
            return Math.abs(diff) < MAX_REQUEST_AGE_MS;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validateSignature(HttpServletRequest request, String signature) {
        try {
            String payload = request.getRequestURI() + request.getQueryString();
            String expected = hmacSha256(payload, aiSecret);
            return expected.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    private String hmacSha256(String data, String secret) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(secret.getBytes(),
                "HmacSHA256");

        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes());

        return java.util.Base64.getEncoder().encodeToString(hash);
    }
}
