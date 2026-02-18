package com.authserver.authserver.code_note.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.controllers.BaseController;
import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.code_note.CodeNoteService;
import com.authserver.authserver.code_note.entry.AddCodeNoteRequest;
import com.authserver.authserver.code_note.entry.CodeNoteEntry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/code-note")
public class CodeNoteController  extends BaseController<Long, CodeNoteEntry, CodeNoteService>{

    @PostMapping("/add-by-position")
    public ResponseEntity<BaseResponse<CodeNoteEntry>> add(@RequestBody AddCodeNoteRequest request) {
        return service.addByPosition(request);
    }
    
}
