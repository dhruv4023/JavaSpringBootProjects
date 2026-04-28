package com.authserver.authserver.expense_tracker.manager;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.authserver.authserver.base.ConvertorInterface;
import com.authserver.authserver.expense_tracker.entry.TransactionEntry;
import com.authserver.authserver.expense_tracker.models.TransactionModel;
import com.authserver.authserver.expense_tracker.repositories.TransactionRepository;
import com.authserver.authserver.user.manager.ResBaseManager;
import com.authserver.authserver.user.util.SecurityUtils;

@Component("expenseTransactionManager")
public class TransactionManager extends ResBaseManager<Long, TransactionEntry, TransactionModel, TransactionRepository> {

    private final ConvertorInterface<TransactionEntry, TransactionModel> transactionConvertor;

    public TransactionManager(TransactionRepository repository, SecurityUtils securityUtils,
            ConvertorInterface<TransactionEntry, TransactionModel> transactionConvertor) {
        super(repository, "transaction", securityUtils);
        this.transactionConvertor = transactionConvertor;
    }

    @Override
    protected TransactionModel toEntity(TransactionEntry entry, TransactionModel existing) {
        Long userId = securityutil.getCurrentUserId();
        if (Objects.nonNull(userId)) {
            entry.setUserId(userId);
        }
        return transactionConvertor.toModel(entry, existing);
    }

    @Override
    protected TransactionEntry toEntry(TransactionModel entity) {
        return transactionConvertor.toEntry(entity);
    }

    public Page<TransactionEntry> getByUserIdAndLabelId(Long labelId, long page, long size) {
        Sort sort = getSort();
        Objects.requireNonNull(sort, "Sort must not be null");
        Pageable pageable = PageRequest.of((int) page, (int) size, sort);
        Long userId = securityutil.getCurrentUserId();
        Page<TransactionModel> entityPage = repository.findByUserIdAndLabelId(userId, labelId, pageable);
        return entityPage.map(this::toEntry);
    }

    @Override
    protected Sort getSort() {
        return Sort.by("date").descending();
    }

}
