package com.authserver.authserver.expense_tracker.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.expense_tracker.models.TransactionModel;
import com.authserver.authserver.user.repositories.UserScopedRepository;

@Repository
public interface TransactionRepository extends UserScopedRepository<TransactionModel, Long> {

    Page<TransactionModel> findByUserIdAndLabelId(Long userId, Long labelId, Pageable pageable);

}
