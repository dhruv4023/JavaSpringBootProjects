package com.authserver.authserver.user.services.auth;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.user.entry.ForgotPasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.manager.auth.AuthManager;
import com.authserver.authserver.user.response.AuthResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService implements AuthServiceInterface {

    AuthManager authManager;

    public AuthService(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public BaseResponse<Void> signup(SignupEntry signupEntry) {
        authManager.signup(signupEntry);
        return new BaseResponse<>(true, "User registered successfully");
    }

    @Override
    public BaseResponse<AuthResponse> login(LoginEntry loginEntry) {
        return new BaseResponse<AuthResponse>(true, "Login successful",
                Collections.singletonList(authManager.login(loginEntry)), null);
    }

    @Override
    public BaseResponse<Void> forgotPassword(ForgotPasswordEntry forgotPasswordEntry) {
        authManager.forgotPassword(forgotPasswordEntry);
        return new BaseResponse<>(true, "Password reset email sent successfully");
    }

}
