package com.authserver.authserver.expense_tracker.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.authserver.authserver.base.BaseRepository;
import com.authserver.authserver.expense_tracker.models.LabelModel;

@Repository
public interface LabelRepository extends BaseRepository<LabelModel, Long> {

    boolean existsByLabelNameAndUserId(String labelName, Long userId);
    boolean existsByParentId(Long parentId);
    Optional<LabelModel> findByDefaultLabelTrueAndUserId(Long userId);
}
