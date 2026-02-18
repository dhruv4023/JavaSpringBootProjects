package com.authserver.authserver.code_note;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseManager;

import jakarta.persistence.EntityNotFoundException;

@Component
public class CodeNoteManager extends BaseManager<Long, CodeNoteEntry, CodeNoteModel,  CodeNoteRepository>{

    private final CodeNoteConvertor codeNoteConvertor;

    protected CodeNoteManager(CodeNoteRepository repository, CodeNoteConvertor codeNoteConvertor) {
        super(repository, "code note");
        this.codeNoteConvertor = codeNoteConvertor;
    }

    @Override
    protected CodeNoteModel toEntity(CodeNoteEntry entry, CodeNoteModel existing) throws EntityNotFoundException {
        return codeNoteConvertor.toModel(entry, existing);
    }

    @Override
    protected CodeNoteEntry toEntry(CodeNoteModel entity) throws EntityNotFoundException {
        return codeNoteConvertor.toEntry(entity);    
    }
}
