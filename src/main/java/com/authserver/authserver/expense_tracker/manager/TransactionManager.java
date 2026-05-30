package com.authserver.authserver.expense_tracker.manager;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.base.ConvertorInterface;
import com.authserver.authserver.expense_tracker.entry.TransactionEntry;
import com.authserver.authserver.expense_tracker.models.TransactionModel;
import com.authserver.authserver.expense_tracker.repositories.TransactionRepository;
import com.authserver.authserver.user.util.SecurityUtils;

@Component("expenseTransactionManager")
public class TransactionManager extends BaseManager<UUID, TransactionEntry, TransactionModel, TransactionRepository> {

    private final ConvertorInterface<TransactionEntry, TransactionModel> transactionConvertor;

    private final SecurityUtils securityutil;

    public TransactionManager(TransactionRepository repository, SecurityUtils securityUtils,
            ConvertorInterface<TransactionEntry, TransactionModel> transactionConvertor) {
        super(repository, "transaction");
        this.securityutil = securityUtils;
        this.transactionConvertor = transactionConvertor;
    }

    @Override
    protected TransactionModel toEntity(TransactionEntry entry, TransactionModel existing) {
        UUID userId = securityutil.getCurrentUserUuid();
        if (Objects.nonNull(userId)) {
            entry.setUserUuid(userId);
        }
        return transactionConvertor.toModel(entry, existing);
    }

    @Override
    protected TransactionEntry toEntry(TransactionModel entity) {
        return transactionConvertor.toEntry(entity);
    }

    public Page<TransactionEntry> getByUserIdAndLabelId(UUID labelUuid, long page, long size) {
        Sort sort = getSort();
        Objects.requireNonNull(sort, "Sort must not be null");
        Pageable pageable = PageRequest.of((int) page, (int) size, sort);
        UUID userId = securityutil.getCurrentUserUuid();
        Page<TransactionModel> entityPage = repository.findByUserUuidAndLabelUuid(userId, labelUuid, pageable);
        return entityPage.map(this::toEntry);
    }

    @Override
    protected Sort getSort() {
        return Sort.by("date").descending();
    }

    @Override
    protected Specification<TransactionModel> buildSpecification(String search) {
        return (root, query, cb) -> cb.equal(root.get("user").get("uuid"), securityutil.getCurrentUserUuid());
    }
}
