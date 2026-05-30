package com.authserver.authserver.user.repositories;

import com.authserver.authserver.user.models.RefreshTokenModel;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshTokenModel, UUID> {
    Optional<RefreshTokenModel> findByToken(String token);

    int deleteByUser(UserModel user);
}
