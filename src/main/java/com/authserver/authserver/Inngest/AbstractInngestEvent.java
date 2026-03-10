package com.authserver.authserver.Inngest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractInngestEvent {

    private static final String INNGEST_URL = "http://127.0.0.1:8288/e/dev_key";
    private static final RestTemplate restTemplate = new RestTemplate();

    protected static void sendEvent(String eventName, Map<String, Object> data) {

        Map<String, Object> event = new HashMap<>();
        event.put("name", eventName);
        event.put("data", data);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(event, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(INNGEST_URL, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to send event: {}", eventName);
            throw new RuntimeException("Inngest event not sent");
        }

        log.info("Event sent successfully: {}", eventName);
    }
}
