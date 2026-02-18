package com.authserver.authserver.code_note.convertor;

import com.authserver.authserver.user.util.SecurityUtils;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseConvertorInterface;
import com.authserver.authserver.code_note.Models.Notebook;
import com.authserver.authserver.code_note.entry.NotebookEntry;
import com.authserver.authserver.user.models.UserModel;

@Component
public class NotebookConvertor implements BaseConvertorInterface<NotebookEntry, Notebook> {
    private final SecurityUtils securityUtils;

    public NotebookConvertor(SecurityUtils securityUtils) {
        this.securityUtils = securityUtils;
    }

    @Override
    public Notebook toModel(NotebookEntry entry, Notebook existing) {

        Notebook model = existing == null ? new Notebook() : existing;

        if (Objects.nonNull(entry.getId())) {
            model.setId(entry.getId());
        }

        if (Objects.nonNull(entry.getName())) {
            model.setName(entry.getName());
        }

        if (Objects.nonNull(entry.getDescription())) {
            model.setDescription(entry.getDescription());
        }

        model.setUser(new UserModel(securityUtils.getCurrentUserId()));

        return model;
    }

    @Override
    public NotebookEntry toEntry(Notebook model) {

        return new NotebookEntry(
                model.getId(),
                model.getName(),
                model.getDescription(),
                List.of(),
                model.getUser() != null ? model.getUser().getId() : null);
    }
}
