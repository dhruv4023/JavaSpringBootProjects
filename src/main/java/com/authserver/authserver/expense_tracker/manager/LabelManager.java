package com.authserver.authserver.expense_tracker.manager;

import com.authserver.authserver.expense_tracker.repositories.TransactionRepository;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.base.ConvertorInterface;
import com.authserver.authserver.base.exception.ResourceNotFoundException;
import com.authserver.authserver.expense_tracker.entry.LabelEntry;
import com.authserver.authserver.expense_tracker.exceptions.LabelDeleteException;
import com.authserver.authserver.expense_tracker.models.LabelModel;
import com.authserver.authserver.expense_tracker.repositories.LabelRepository;
import com.authserver.authserver.user.util.SecurityUtils;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Component
public class LabelManager extends BaseManager<Long, LabelEntry, LabelModel, LabelRepository> {

    @Override
    @Transactional
    protected Boolean validateAddEntry(LabelModel entity) {
        if (Objects.isNull(entity.getDefaultLabel())) {
            entity.setDefaultLabel(false);
        }

        if (entity.getParent() == null) {
            handleDefaultLabel(entity);
            return true;
        }

        LabelModel parent = repository.findById(entity.getParent().getId())
                .orElseThrow(() -> new RuntimeException("Parent label not found"));
        if (parent.getParent() != null) {
            throw new RuntimeException(
                    "Maximum 2 levels of labels allowed");
        }

        handleDefaultLabel(entity);
        return true;
    }

    @Transactional
    @Override
    protected Boolean validateUpdateEntry(LabelEntry newEntry,
            LabelEntry existing) {
        if (newEntry.getParentId() == null) {
            handleDefaultLabel(toEntity(newEntry, null));
            return true;
        }

        LabelEntry parent = getById(newEntry.getParentId());
        if (parent.getId().equals(existing.getId())) {
            throw new RuntimeException("Label cannot be parent of itself");
        }

        if (parent.getParentId() != null) {
            throw new RuntimeException(
                    "Maximum 2 levels of labels allowed");
        }
        handleDefaultLabel(toEntity(newEntry, null));
        return true;
    }

    private final TransactionRepository transactionRepository;
    private final ConvertorInterface<LabelEntry, LabelModel> labelConvertor;
    private final SecurityUtils securityutil;

    public LabelManager(LabelRepository repository, SecurityUtils securityUtils,
            ConvertorInterface<LabelEntry, LabelModel> labelConvertor, TransactionRepository transactionRepository) {
        super(repository, "label");
        this.securityutil = securityUtils;
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
        LabelEntry entry = getById(id);
        if (entry.getDefaultLabel()) {
            throw new LabelDeleteException("default label");
        }
        if (repository.existsByParentId(id)) {
            throw new LabelDeleteException("sub labels");
        }
        if (transactionRepository.existsByLabelId(id)) {
            throw new LabelDeleteException("transactions");
        }
        super.delete(id);
    }

    @Transactional
    private void handleDefaultLabel(LabelModel label) {

        if (!Boolean.TRUE.equals(label.getDefaultLabel())) {
            return;
        }

        repository.findByDefaultLabelTrueAndUserId(securityutil.getCurrentUserId())
                .ifPresent(currentDefault -> {
                    if (!currentDefault.getId().equals(label.getId())) {
                        currentDefault.setDefaultLabel(false);
                        repository.save(currentDefault);
                    }
                });
    }

    @Override
    protected Specification<LabelModel> buildSpecification(String search) {
        return (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("user").get("id"), securityutil.getCurrentUserId());
            if (search == null || search.trim().isEmpty()) {
                return predicate;
            }
            return cb.and(predicate, cb.like(cb.lower(root.get("labelName")), search.toLowerCase() + "%"));
        };
    }
}
