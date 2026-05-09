package com.authserver.authserver.user.manager;

import com.authserver.authserver.user.exceptions.UnauthorizedException;
import com.authserver.authserver.user.models.RefreshTokenModel;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.user.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenManager implements RefreshTokenManagerInterface {

    @Value("${jwt.refresh.expirationMs:604800000}") // Default 7 days
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshTokenModel createRefreshToken(UserModel user) {
        RefreshTokenModel refreshToken = new RefreshTokenModel();

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshTokenModel verifyExpiration(RefreshTokenModel token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new UnauthorizedException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Override
    @Transactional
    public void deleteByUserId(UserModel user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    public RefreshTokenModel findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Refresh token is not in database!"));
    }
}
