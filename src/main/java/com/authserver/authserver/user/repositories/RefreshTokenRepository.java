package com.authserver.authserver.user.repositories;

import com.authserver.authserver.user.models.RefreshTokenModel;
import com.authserver.authserver.user.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, Long> {
    Optional<RefreshTokenModel> findByToken(String token);
    int deleteByUser(UserModel user);
}
