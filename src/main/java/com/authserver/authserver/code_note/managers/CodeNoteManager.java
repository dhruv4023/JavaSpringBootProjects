package com.authserver.authserver.code_note.managers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.code_note.InjestEvent;
import com.authserver.authserver.code_note.Models.CodeNoteModel;
import com.authserver.authserver.code_note.convertor.CodeNoteConvertor;
import com.authserver.authserver.code_note.entry.CodeNoteEntry;
import com.authserver.authserver.code_note.repository.CodeNoteRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class CodeNoteManager extends BaseManager<Long, CodeNoteEntry, CodeNoteModel, CodeNoteRepository> {

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

    @Override
    @Transactional
    public CodeNoteEntry add(CodeNoteEntry entry) throws EntityNotFoundException {
        CodeNoteEntry note = super.add(entry);
        InjestEvent.triggerNoteCreatedEvent(note);
        return note;
    }
}
