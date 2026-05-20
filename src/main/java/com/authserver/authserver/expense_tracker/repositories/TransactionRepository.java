package com.authserver.authserver.expense_tracker.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.base.BaseRepository;
import com.authserver.authserver.expense_tracker.models.TransactionModel;

@Repository
public interface TransactionRepository extends BaseRepository<TransactionModel, Long> {

    Page<TransactionModel> findByUserIdAndLabelId(Long userId, Long labelId, Pageable pageable);

    boolean existsByLabelId(Long labelId);
}
