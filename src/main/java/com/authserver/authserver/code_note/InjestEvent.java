package com.authserver.authserver.code_note;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.authserver.authserver.code_note.entry.CodeNoteEntry;

import java.util.*;

public class InjestEvent {

    public static void triggerNoteCreatedEvent(CodeNoteEntry note) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8288/e";

        Map<String, Object> event = new HashMap<>();
        event.put("name", "note/created");

        Map<String, Object> data = new HashMap<>();
        data.put("noteId", note.getId());
        data.put("title", note.getTitle());
        data.put("note", note.getNote());
        data.put("permanentLink", note.getPermanentLink());
        event.put("data", data);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(event, headers);

        restTemplate.postForEntity(url, request, String.class);
    }
}
