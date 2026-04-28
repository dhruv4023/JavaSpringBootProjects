package com.authserver.authserver.expense_tracker.repositories;

import org.springframework.stereotype.Repository;

import com.authserver.authserver.expense_tracker.models.LabelModel;
import com.authserver.authserver.user.repositories.UserScopedRepository;

@Repository
public interface LabelRepository extends UserScopedRepository<LabelModel, Long> {

    boolean existsByLabelNameAndUserId(String labelName, Long userId);

}
