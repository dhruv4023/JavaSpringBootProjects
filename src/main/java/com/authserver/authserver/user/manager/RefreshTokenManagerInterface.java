package com.authserver.authserver.user.manager;

import com.authserver.authserver.user.models.RefreshTokenModel;
import com.authserver.authserver.user.models.UserModel;

public interface RefreshTokenManagerInterface {
    RefreshTokenModel createRefreshToken(UserModel user);
    RefreshTokenModel verifyExpiration(RefreshTokenModel token);
    void deleteByUserId(UserModel user);
    RefreshTokenModel findByToken(String token);
}
