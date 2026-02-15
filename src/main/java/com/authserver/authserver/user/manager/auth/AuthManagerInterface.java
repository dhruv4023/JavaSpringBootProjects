package com.authserver.authserver.user.manager.auth;

import com.authserver.authserver.user.entry.ForgotPasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.response.AuthResponse;

public interface AuthManagerInterface {
    
    public void signup(SignupEntry signupEntry);

    public AuthResponse login(LoginEntry loginEntry);

    public void forgotPassword(ForgotPasswordEntry forgotPasswordEntry);
}
