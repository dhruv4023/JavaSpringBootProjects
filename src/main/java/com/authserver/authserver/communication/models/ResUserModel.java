package com.authserver.authserver.communication.models;

import com.authserver.authserver.user.models.UserModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;

public class ResUserModel extends UserModel {
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmailCredentials emailCredentials;
}
