package com.authserver.authserver.user.entry;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}
