package com.authserver.authserver.expense_tracker.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.ConvertorInterface;
import com.authserver.authserver.expense_tracker.entry.LabelEntry;
import com.authserver.authserver.expense_tracker.models.LabelModel;
import com.authserver.authserver.user.models.UserModel;

@Component
public class LabelConvertor implements ConvertorInterface<LabelEntry, LabelModel> {

    @Override
    public LabelModel toModel(LabelEntry entry, LabelModel existing) {
        LabelModel label = existing != null ? existing : new LabelModel();
        if (Objects.nonNull(entry.getLabelName())) {
            label.setLabelName(entry.getLabelName());
        }
        if (Objects.nonNull(entry.getDefaultLabel())) {
            label.setDefaultLabel(entry.getDefaultLabel());
        }
        if (Objects.nonNull(entry.getParentUuid())) {
            LabelModel parent = new LabelModel();
            parent.setUuid(entry.getParentUuid());
            label.setParent(parent);
        }
        if (Objects.nonNull(entry.getUserUuid())) {
            UserModel user = new UserModel();
            user.setUuid(entry.getUserUuid());
            label.setUser(user);
        }
        return label;
    }

    @Override
    public LabelEntry toEntry(LabelModel model) {
        LabelEntry entry = new LabelEntry();
        entry.setUuid(model.getUuid());
        entry.setLabelName(model.getLabelName());
        entry.setDefaultLabel(model.getDefaultLabel());
        entry.setParentUuid(model.getParent() != null ? model.getParent().getUuid() : null);
        entry.setUserUuid(model.getUser() != null ? model.getUser().getUuid() : null);
        return entry;
    }

}
