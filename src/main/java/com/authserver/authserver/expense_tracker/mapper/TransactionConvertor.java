package com.authserver.authserver.expense_tracker.mapper;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.authserver.authserver.base.ConvertorInterface;
import com.authserver.authserver.expense_tracker.entry.LabelEntry;
import com.authserver.authserver.expense_tracker.entry.TransactionEntry;
import com.authserver.authserver.expense_tracker.models.LabelModel;
import com.authserver.authserver.expense_tracker.models.TransactionModel;
import com.authserver.authserver.expense_tracker.repositories.LabelRepository;
import com.authserver.authserver.user.models.UserModel;

import jakarta.persistence.EntityNotFoundException;

@Component
public class TransactionConvertor implements ConvertorInterface<TransactionEntry, TransactionModel> {

    @Autowired
    private LabelRepository labelRepository;

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
        if (Objects.nonNull(entry.getLabel())) {
            LabelModel label = labelRepository.findById(entry.getLabel().getUuid())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Label not found with id " + entry.getLabel().getUuid()));
            transaction.setLabel(label);
        }
        if (Objects.nonNull(entry.getUserUuid())) {
            UserModel user = new UserModel();
            user.setUuid(entry.getUserUuid());
            transaction.setUser(user);
        }
        return transaction;
    }

    @Override
    public TransactionEntry toEntry(TransactionModel model) {
        TransactionEntry entry = new TransactionEntry();
        entry.setUuid(model.getUuid());
        entry.setComment(model.getComment());
        entry.setAmt(model.getAmt());
        entry.setDate(model.getDate());
        if (model.getLabel() != null) {
            entry.setLabel(new LabelEntry(model.getLabel().getUuid(), model.getLabel().getLabelName()));
        }
        entry.setUserUuid(model.getUser() != null ? model.getUser().getUuid() : null);
        return entry;
    }

}
