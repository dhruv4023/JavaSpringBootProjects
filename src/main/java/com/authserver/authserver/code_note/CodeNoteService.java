package com.authserver.authserver.code_note;

import org.springframework.stereotype.Service;

import com.authserver.authserver.base.service.BaseService;

@Service
public class CodeNoteService extends BaseService<Long, CodeNoteEntry, CodeNoteManager>{

    public CodeNoteService(CodeNoteManager manager) {
        super(manager);
    }
    
}
