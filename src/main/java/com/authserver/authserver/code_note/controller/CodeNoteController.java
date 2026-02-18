package com.authserver.authserver.code_note.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.controllers.BaseController;
import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.code_note.entry.AddCodeNoteRequest;
import com.authserver.authserver.code_note.entry.AddPrCodeNoteRequest;
import com.authserver.authserver.code_note.entry.CodeNoteEntry;
import com.authserver.authserver.code_note.service.CodeNoteService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/code-note")
public class CodeNoteController extends BaseController<Long, CodeNoteEntry, CodeNoteService> {

    @PostMapping("/add-by-position")
    public ResponseEntity<BaseResponse<CodeNoteEntry>> add(@RequestBody AddCodeNoteRequest request) {
        return service.addByPosition(request);
    }

    @PostMapping("/add-pr-notes-by-position")
    public ResponseEntity<BaseResponse<CodeNoteEntry>> add(@RequestBody AddPrCodeNoteRequest request) {
        return service.addPrNotesByPosition(request);
    }

    @GetMapping("/notebook/{notebookId}")
    public ResponseEntity<BaseResponse<CodeNoteEntry>> getMethodName(@PathVariable Long notebookId,
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return service.getManyCommitFIleByNotebookId(notebookId, page, size);
    }

}
