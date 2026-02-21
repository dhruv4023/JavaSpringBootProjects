package com.authserver.authserver.code_note.convertor;

import java.util.Objects;
import java.util.stream.Collectors;

import com.authserver.authserver.user.util.SecurityUtils;
import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseConvertorInterface;
import com.authserver.authserver.code_note.Models.CodeNoteModel;
import com.authserver.authserver.code_note.entry.CodeNoteEntry;
import com.authserver.authserver.code_note.managers.TagManager;
import com.authserver.authserver.user.manager.UserManager;

@Component
public class CodeNoteConvertor implements BaseConvertorInterface<CodeNoteEntry, CodeNoteModel> {

    private final TagManager tagManager;

    private final SecurityUtils securityUtils;

    private final UserManager userManager;

    public CodeNoteConvertor(UserManager userManager, SecurityUtils securityUtils, TagManager tagManager) {
        this.userManager = userManager;
        this.securityUtils = securityUtils;
        this.tagManager = tagManager;
    }

    @Override
    public CodeNoteModel toModel(CodeNoteEntry entry, CodeNoteModel existing) {
        CodeNoteModel model = existing == null ? new CodeNoteModel() : existing;
        if (Objects.nonNull(entry.getId()))
            model.setId(entry.getId());
        if (Objects.nonNull(entry.getPermanentLink())) {
            model.setPermanentLink(entry.getPermanentLink());
        }
        if (Objects.nonNull(entry.getNote())) {
            model.setNote(entry.getNote());
        }
        if (Objects.nonNull(entry.getTitle())) {
            model.setTitle(entry.getTitle());
        }
        if (Objects.nonNull(entry.getAiSummary())) {
            model.setAiSummary(entry.getAiSummary());
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
        if (Objects.nonNull(entry.getAiTags())) {
            model.setAiTags(
                    entry.getAiTags()
                            .stream()
                            .map(tag -> tagManager.addTag(tag))
                            .collect(Collectors.toSet()));
        }
        if (Objects.isNull(model.getUser())) {
            model.setUser(userManager.findUserModelByUsername(securityUtils.getCurrentUsername()));
        }
        return model;
    }

    @Override
    public CodeNoteEntry toEntry(CodeNoteModel model) {
        return new CodeNoteEntry(
                model.getId(),
                model.getPermanentLink(),
                model.getNote(),
                model.getTitle(),
                model.getAiSummary(),
                model.getAiExplanation(),
                model.getAiImprovements(),
                model.getAiEmbeddingId(),
                model.getAiTags().stream().map(tag -> tag.getName()).collect(Collectors.toSet()),
                model.getUser().getId());
    }

}
