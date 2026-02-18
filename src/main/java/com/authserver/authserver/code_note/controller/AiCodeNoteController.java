package com.authserver.authserver.code_note.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.code_note.entry.CodeNoteEntry;
import com.authserver.authserver.code_note.service.CodeNoteService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/ai/code-note")
public class AiCodeNoteController {

    private final CodeNoteService codeNoteService;

    AiCodeNoteController(CodeNoteService codeNoteService) {
        this.codeNoteService = codeNoteService;
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<CodeNoteEntry>> updateCodeNote(@RequestBody CodeNoteEntry entry,
            @PathVariable Long id) {
        return codeNoteService.update(id, entry);
    }
}
