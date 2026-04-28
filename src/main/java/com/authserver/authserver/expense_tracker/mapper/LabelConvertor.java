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
        if (Objects.nonNull(entry.getParentId())) {
            LabelModel parent = new LabelModel();
            parent.setId(entry.getParentId());
            label.setParent(parent);
        }
        if (Objects.nonNull(entry.getUserId())) {
            UserModel user = new UserModel();
            user.setId(entry.getUserId());
            label.setUser(user);
        }
        return label;
    }

    @Override
    public LabelEntry toEntry(LabelModel model) {
        LabelEntry entry = new LabelEntry();
        entry.setId(model.getId());
        entry.setLabelName(model.getLabelName());
        entry.setDefaultLabel(model.isDefaultLabel());
        entry.setParentId(model.getParent() != null ? model.getParent().getId() : null);
        entry.setUserId(model.getUser() != null ? model.getUser().getId() : null);
        return entry;
    }

}
