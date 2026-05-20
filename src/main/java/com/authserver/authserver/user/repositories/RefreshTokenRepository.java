package com.authserver.authserver.user.repositories;

import com.authserver.authserver.user.models.RefreshTokenModel;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends BaseRepository<RefreshTokenModel, Long> {
    Optional<RefreshTokenModel> findByToken(String token);

    int deleteByUser(UserModel user);
}
