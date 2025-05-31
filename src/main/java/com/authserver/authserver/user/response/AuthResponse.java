package com.authserver.authserver.user.response;

import com.authserver.authserver.user.entry.UserEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private UserEntry user;
    private String token;
}

