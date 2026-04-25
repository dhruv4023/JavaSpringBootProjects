package com.authserver.authserver.google_auth;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class GoogleUser {

    private String googleId;
    private String email;
    private String name;

    public GoogleUser(String googleId, String email, String name) {
        this.googleId = googleId;
        this.email = email;
        this.name = name;
    }

}