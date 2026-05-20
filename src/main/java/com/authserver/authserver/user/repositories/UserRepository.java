package com.authserver.authserver.user.repositories;

import com.authserver.authserver.base.BaseRepository;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.user.models.UserModel;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findByEmail(String email);

    boolean existsBy();

    boolean existsByEmail(String email);
}
