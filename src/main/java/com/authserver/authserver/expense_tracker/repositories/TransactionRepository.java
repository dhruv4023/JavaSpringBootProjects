package com.authserver.authserver.expense_tracker.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.base.BaseRepository;
import com.authserver.authserver.expense_tracker.models.TransactionModel;

@Repository
public interface TransactionRepository extends BaseRepository<TransactionModel, UUID> {

    Page<TransactionModel> findByUserUuidAndLabelUuid(UUID userUuid, UUID labelUuid, Pageable pageable);

    boolean existsByUserUuidAndLabelUuid(UUID userUuid, UUID labelUuid);
}
