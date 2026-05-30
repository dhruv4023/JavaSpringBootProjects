package com.authserver.authserver.expense_tracker.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.authserver.authserver.base.BaseRepository;
import com.authserver.authserver.expense_tracker.models.LabelModel;

@Repository
public interface LabelRepository extends BaseRepository<LabelModel, UUID> {

    boolean existsByLabelNameAndUserUuid(String labelName, UUID userUuid);

    boolean existsByParentUuid(UUID parentUuid);

    Optional<LabelModel> findByDefaultLabelTrueAndUserUuid(UUID userUuid);
}
