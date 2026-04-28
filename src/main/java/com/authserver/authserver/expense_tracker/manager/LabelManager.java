package com.authserver.authserver.expense_tracker.manager;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.ConvertorInterface;
import com.authserver.authserver.expense_tracker.entry.LabelEntry;
import com.authserver.authserver.expense_tracker.models.LabelModel;
import com.authserver.authserver.expense_tracker.repositories.LabelRepository;
import com.authserver.authserver.user.manager.ResBaseManager;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.user.util.SecurityUtils;

@Component
public class LabelManager extends ResBaseManager<Long, LabelEntry, LabelModel, LabelRepository> {

    private final ConvertorInterface<LabelEntry, LabelModel> labelConvertor;

    public LabelManager(LabelRepository repository, SecurityUtils securityUtils,
            ConvertorInterface<LabelEntry, LabelModel> labelConvertor) {
        super(repository, "label", securityUtils);
        this.labelConvertor = labelConvertor;
    }

    @Override
    protected LabelModel toEntity(LabelEntry entry, LabelModel existing) {
        Long userId = securityutil.getCurrentUserId();
        if (Objects.nonNull(userId)) {
            entry.setUserId(userId);
        }
        return labelConvertor.toModel(entry, existing);
    }

    @Override
    protected LabelEntry toEntry(LabelModel entity) {
        return labelConvertor.toEntry(entity);
    }

}
