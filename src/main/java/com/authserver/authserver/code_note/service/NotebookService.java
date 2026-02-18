package com.authserver.authserver.code_note.service;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.base.service.BaseService;
import com.authserver.authserver.code_note.entry.NotebookEntry;
import com.authserver.authserver.code_note.managers.NotebookManager;

@Service
public class NotebookService extends BaseService<Long, NotebookEntry, NotebookManager> {

    public NotebookService(NotebookManager manager) {
        super(manager);
    }

    public ResponseEntity<BaseResponse<String>> chatOnNotebook(String query, Long notebookId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse<>(true, "generated answer successfully", Collections.singletonList(manager.askAI(query, notebookId)), null));
    }
}
