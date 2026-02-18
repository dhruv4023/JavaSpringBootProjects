package com.authserver.authserver.code_note;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.controllers.BaseController;

@RestController
@RequestMapping("/code-note")
public class CodeNoteController  extends BaseController<Long, CodeNoteEntry, CodeNoteService>{
    
}
