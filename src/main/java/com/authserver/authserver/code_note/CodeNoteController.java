package com.authserver.authserver.code_note;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.controllers.BaseController;
import com.authserver.authserver.code_note.entry.CodeNoteEntry;

@RestController
@RequestMapping("/code-note")
public class CodeNoteController  extends BaseController<Long, CodeNoteEntry, CodeNoteService>{
    
}
