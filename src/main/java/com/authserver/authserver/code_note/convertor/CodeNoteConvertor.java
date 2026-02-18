package com.authserver.authserver.code_note.convertor;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseConvertorInterface;
import com.authserver.authserver.code_note.Models.CodeNote;
import com.authserver.authserver.code_note.Models.Notebook;
import com.authserver.authserver.code_note.entry.CodeNoteEntry;
import com.authserver.authserver.code_note.managers.TagManager;

@Component
public class CodeNoteConvertor implements BaseConvertorInterface<CodeNoteEntry, CodeNote> {

    private final TagManager tagManager;

    public CodeNoteConvertor(TagManager tagManager) {
        this.tagManager = tagManager;
    }

    @Override
    public CodeNote toModel(CodeNoteEntry entry, CodeNote existing) {
        CodeNote model = existing == null ? new CodeNote() : existing;
        if (Objects.nonNull(entry.getId())) {
            model.setId(entry.getId());
        }
        if (Objects.nonNull(entry.getNotebookId())) {
            model.setNotebook(new Notebook(entry.getNotebookId()));
        }
        if (Objects.nonNull(entry.getPermanentLink())) {
            model.setPermanentLink(entry.getPermanentLink());
        }
        if (Objects.nonNull(entry.getDescription())) {
            model.setNote(entry.getDescription());
        }
        if (Objects.nonNull(entry.getTitle())) {
            model.setTitle(entry.getTitle());
        }
        if (Objects.nonNull(entry.getAiSummary())) {
            model.setAiSummary(entry.getAiSummary());
        }
        if (Objects.nonNull(entry.getPosition())) {
            model.setPosition(entry.getPosition());
        }
        if (Objects.nonNull(entry.getAiExplanation())) {
            model.setAiExplanation(entry.getAiExplanation());
        }
        if (Objects.nonNull(entry.getAiImprovements())) {
            model.setAiImprovements(entry.getAiImprovements());
        }
        if (Objects.nonNull(entry.getAiEmbeddingId())) {
            model.setAiEmbeddingId(entry.getAiEmbeddingId());
        }
        if (Objects.nonNull(entry.getAiDescription())) {
            model.setAiDescription(entry.getAiDescription());
        }
        if (Objects.nonNull(entry.getAiTags())) {
            model.setAiTags(
                    entry.getAiTags()
                            .stream()
                            .map(tag -> tagManager.addTag(tag))
                            .collect(Collectors.toSet()));
        }
        return model;
    }

    @Override
    public CodeNoteEntry toEntry(CodeNote model) {
        return new CodeNoteEntry(
                model.getId(),
                model.getNotebook().getId(),
                model.getPermanentLink(),
                model.getNote(),
                model.getTitle(),
                model.getPosition(),
                model.getAiSummary(),
                model.getAiExplanation(),
                model.getAiDescription(),
                model.getAiImprovements(),
                model.getAiEmbeddingId(),
                model.getAiTags().stream().map(tag -> tag.getName()).collect(Collectors.toSet()));
    }

}
