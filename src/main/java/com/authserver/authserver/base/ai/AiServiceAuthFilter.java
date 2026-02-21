package com.authserver.authserver.base.ai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
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

    private static final long MAX_REQUEST_AGE_MS = 30000;

    @Override
    protected void doFilterInternal(
            @SuppressWarnings("null") HttpServletRequest request,
            @SuppressWarnings("null") HttpServletResponse response,
            @SuppressWarnings("null") FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI().startsWith("/api") ? request.getRequestURI()
                .substring(4) : request.getRequestURI();

        if (!requestURI.startsWith("/ai")) {
            filterChain.doFilter(request, response);
            return;
        }
    
        // ===== Validate Content Type =====
        if (!isJsonRequest(request)) {
            response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        // ===== Validate Service Key =====
        if (!validateServiceKey(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // ===== Validate Timestamp =====
        String timestamp = request.getHeader("X-Request-Time");

        if (timestamp == null || !isValidTimestamp(timestamp)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // ===== Validate Signature =====
        String signature = request.getHeader("X-AI-Signature");

        
        if (signature == null || !validateSignature(signature, timestamp, requestURI)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);

    }

    private boolean isJsonRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.contains("application/json");
    }

    private boolean validateServiceKey(HttpServletRequest request) {
        String apiKey = request.getHeader("X-AI-Service-Key");
        return apiKey != null && apiKey.equals(aiServiceKey);
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

    private boolean validateSignature(
            String signature,
            String timestamp,
            String path) {

        try {

            String message = path + "|" + timestamp;

            String expected = hmacSha256(message, aiSecret);

            return MessageDigest.isEqual(
                    expected.getBytes(StandardCharsets.UTF_8),
                    signature.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            return false;
        }
    }


    private String hmacSha256(String message, String secret) throws Exception {

        Mac mac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        mac.init(secretKey);

        byte[] hash = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(hash);
    }
}