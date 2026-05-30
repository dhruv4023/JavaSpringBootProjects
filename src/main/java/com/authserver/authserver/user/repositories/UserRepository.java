package com.authserver.authserver.user.repositories;

import com.authserver.authserver.base.BaseRepository;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.user.models.UserModel;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<UserModel, UUID> {
    Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findByEmail(String email);

    boolean existsBy();

    boolean existsByEmail(String email);
}
