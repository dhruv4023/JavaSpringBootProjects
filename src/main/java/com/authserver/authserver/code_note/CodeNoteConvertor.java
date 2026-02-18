package com.authserver.authserver.code_note;

import java.util.Objects;
import com.authserver.authserver.user.util.SecurityUtils;
import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseConvertorInterface;
import com.authserver.authserver.user.manager.UserManager;

@Component
public class CodeNoteConvertor implements BaseConvertorInterface<CodeNoteEntry, CodeNoteModel> {

    private final SecurityUtils securityUtils;

    private final UserManager userManager;

    public CodeNoteConvertor(UserManager userManager, SecurityUtils securityUtils) {
        this.userManager = userManager;
        this.securityUtils = securityUtils;
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
        model.setUser(userManager.findUserModelByUsername(securityUtils.getCurrentUsername()));
        return model;
    }

    @Override
    public CodeNoteEntry toEntry(CodeNoteModel model) {
        return new CodeNoteEntry(
                model.getId(),
                model.getPermanentLink(),
                model.getNote(),
                model.getTitle(),
                model.getAiDescription(),
                model.getUser().getId());
    }

}
