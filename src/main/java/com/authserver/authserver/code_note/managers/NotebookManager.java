package com.authserver.authserver.code_note.managers;

import com.authserver.authserver.code_note.convertor.NotebookConvertor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.authserver.authserver.code_note.Models.Notebook;
import com.authserver.authserver.code_note.entry.NotebookEntry;
import com.authserver.authserver.code_note.repository.NotebookRepository;
import com.authserver.authserver.user.manager.ResBaseManager;
import com.authserver.authserver.user.util.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;

@Component
public class NotebookManager extends ResBaseManager<Long, NotebookEntry, Notebook, NotebookRepository> {

    private final CodeNoteManager codeNoteManager;
    private final ObjectMapper objectMapper;
    private final NotebookConvertor notebookConvertor;

    protected NotebookManager(NotebookRepository repository, NotebookConvertor notebookConvertor,
            SecurityUtils securityutil, CodeNoteManager codeNoteManager, ObjectMapper objectMapper) {
        super(repository, "notebook", securityutil);
        this.objectMapper = objectMapper;
        this.notebookConvertor = notebookConvertor;
        this.codeNoteManager = codeNoteManager;
    }

    @Override
    protected Notebook toEntity(NotebookEntry entry, Notebook existing) throws EntityNotFoundException {
        return notebookConvertor.toModel(entry, existing);
    }

    @Override
    protected NotebookEntry toEntry(Notebook entity) throws EntityNotFoundException {
        return notebookConvertor.toEntry(entity);
    }

    // @Override
    // public Page<NotebookEntry> getByUserId(long page, long size) {
    // Page<NotebookEntry> entries = super.getByUserId(page, size);
    // entries.forEach(notebook -> notebook
    // .setNotes(codeNoteManager.getManyCommitsByNotebookId(notebook.getId(), page,
    // size).getContent()));
    // return entries;
    // }

    @Override
    public NotebookEntry getById(Long id) throws EntityNotFoundException {
        NotebookEntry notebookEntry = super.getById(id);
        notebookEntry.setNotes(codeNoteManager.getManyCommitsByNotebookId(id, 0, 10).getContent());
        return notebookEntry;
    }

    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${ai.service.key}")
    private String API_KEY;

    @Value("${ai.service.secret}")
    private String API_SECRET;

    @Value("${ai.service.url}")
    private String ASK_URL;

    public String askAI(String query, Long notebookId) {
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("notebookId", notebookId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", API_KEY);
        headers.set("x-api-secret", API_SECRET);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    ASK_URL,
                    request,
                    String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed with status: " + response.getStatusCode());
            }

            Map<String, Object> json = objectMapper.readValue(response.getBody(), Map.class);

            String status = (String) json.get("status");

            if (!"success".equalsIgnoreCase(status)) {
                throw new RuntimeException("API Error: " + json);
            }

            return (String) json.get("answer");
        } catch (Exception e) {
            throw new RuntimeException("Error calling AI API: " + e.getMessage(), e);
        }
    }
}
