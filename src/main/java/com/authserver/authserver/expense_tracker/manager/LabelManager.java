package com.authserver.authserver.expense_tracker.manager;

import com.authserver.authserver.expense_tracker.repositories.TransactionRepository;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.ConvertorInterface;
import com.authserver.authserver.base.exception.ResourceNotFoundException;
import com.authserver.authserver.expense_tracker.entry.LabelEntry;
import com.authserver.authserver.expense_tracker.exceptions.LabelDeleteException;
import com.authserver.authserver.expense_tracker.models.LabelModel;
import com.authserver.authserver.expense_tracker.repositories.LabelRepository;
import com.authserver.authserver.user.manager.ResBaseManager;
import com.authserver.authserver.user.util.SecurityUtils;

@Component
public class LabelManager extends ResBaseManager<Long, LabelEntry, LabelModel, LabelRepository> {

    private final TransactionRepository transactionRepository;
    private final ConvertorInterface<LabelEntry, LabelModel> labelConvertor;

    public LabelManager(LabelRepository repository, SecurityUtils securityUtils,
            ConvertorInterface<LabelEntry, LabelModel> labelConvertor, TransactionRepository transactionRepository) {
        super(repository, "label", securityUtils);
        this.labelConvertor = labelConvertor;
        this.transactionRepository = transactionRepository;
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

    @Override
    public void delete(Long id) throws ResourceNotFoundException {
        if (repository.existsByParentId(id)) {
            throw new LabelDeleteException("sub labels");
        }
        if (transactionRepository.existsByLabelId(id)) {
            throw new LabelDeleteException("transactions");
        }
        super.delete(id);
    }

}
