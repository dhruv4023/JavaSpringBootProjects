package com.authserver.authserver.expense_tracker.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.ConvertorInterface;
import com.authserver.authserver.expense_tracker.entry.TransactionEntry;
import com.authserver.authserver.expense_tracker.models.LabelModel;
import com.authserver.authserver.expense_tracker.models.TransactionModel;
import com.authserver.authserver.user.models.UserModel;

@Component
public class TransactionConvertor implements ConvertorInterface<TransactionEntry, TransactionModel> {

    @Override
    public TransactionModel toModel(TransactionEntry entry, TransactionModel existing) {
        TransactionModel transaction = existing != null ? existing : new TransactionModel();
        if (Objects.nonNull(entry.getComment())) {
            transaction.setComment(entry.getComment());
        }
        if (Objects.nonNull(entry.getAmt())) {
            transaction.setAmt(entry.getAmt());
        }
        if (Objects.nonNull(entry.getDate())) {
            transaction.setDate(entry.getDate());
        }
        if (Objects.nonNull(entry.getLabelId())) {
            LabelModel label = new LabelModel();
            label.setId(entry.getLabelId());
            transaction.setLabel(label);
        }
        if (Objects.nonNull(entry.getUserId())) {
            UserModel user = new UserModel();
            user.setId(entry.getUserId());
            transaction.setUser(user);
        }
        return transaction;
    }

    @Override
    public TransactionEntry toEntry(TransactionModel model) {
        TransactionEntry entry = new TransactionEntry();
        entry.setId(model.getId());
        entry.setComment(model.getComment());
        entry.setAmt(model.getAmt());
        entry.setDate(model.getDate());
        entry.setLabelId(model.getLabel() != null ? model.getLabel().getId() : null);
        entry.setUserId(model.getUser() != null ? model.getUser().getId() : null);
        return entry;
    }

}
