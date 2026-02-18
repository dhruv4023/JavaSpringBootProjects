package com.authserver.authserver.code_note.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.controllers.BaseController;
import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.code_note.entry.NotebookEntry;
import com.authserver.authserver.code_note.service.NotebookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/notebook")
public class NotebookController extends BaseController<Long, NotebookEntry, NotebookService> {

    @PostMapping("/ask/{notebookId}")
    public ResponseEntity<BaseResponse<String>> chatOnNotebook(@RequestBody String query, @PathVariable Long notebookId) {
        return service.chatOnNotebook(query, notebookId);
    }
    
}
