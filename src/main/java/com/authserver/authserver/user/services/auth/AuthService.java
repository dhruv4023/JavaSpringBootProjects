package com.authserver.authserver.user.services.auth;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.user.entry.ChangePasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.manager.auth.AuthManagerInterface;
import com.authserver.authserver.user.response.AuthResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService implements AuthServiceInterface {

    AuthManagerInterface authManager;

    public AuthService(AuthManagerInterface authManager) {
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
    public BaseResponse<Void> forgotPassword(String userName) {
        authManager.forgotPassword(userName);
        return new BaseResponse<>(true, "Password reset email sent successfully");
    }

    @Override
    public BaseResponse<Void> changePassword(ChangePasswordEntry changePasswordEntry) {
        authManager.changePassword(changePasswordEntry);
        return new BaseResponse<>(true, "Password changed successfully");
    }

}
