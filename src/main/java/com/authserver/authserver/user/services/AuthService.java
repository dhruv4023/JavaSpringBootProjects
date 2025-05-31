package com.authserver.authserver.user.services;

import com.authserver.authserver.base.helper.ResponseObject;
import com.authserver.authserver.user.entry.ForgotPasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;

public interface AuthService {
    public ResponseObject signup(SignupEntry signupEntry);

    public ResponseObject login(LoginEntry loginEntry);

    public ResponseObject forgotPassword(ForgotPasswordEntry forgotPasswordEntry);
}
