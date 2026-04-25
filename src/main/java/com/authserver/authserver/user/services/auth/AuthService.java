package com.authserver.authserver.user.services.auth;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.google_auth.GoogleTokenVerifierService;
import com.authserver.authserver.google_auth.GoogleUser;
import com.authserver.authserver.google_auth.TokenRequest;
import com.authserver.authserver.user.entry.ChangePasswordEntry;
import com.authserver.authserver.user.entry.ForgotPasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.manager.auth.AuthManagerInterface;
import com.authserver.authserver.user.response.AuthResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService implements AuthServiceInterface {

    AuthManagerInterface authManager;
    private final GoogleTokenVerifierService googleTokenVerifierService;


    public AuthService(AuthManagerInterface authManager, GoogleTokenVerifierService googleTokenVerifierService) {
        this.authManager = authManager;
        this.googleTokenVerifierService = googleTokenVerifierService;
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
        return new BaseResponse<>(true, "We received your request to reset your password. Please check your email in few minutes.");
    }

    @Override
    public BaseResponse<Void> changePassword(ChangePasswordEntry changePasswordEntry) {
        authManager.changePassword(changePasswordEntry);
        return new BaseResponse<>(true, "Password changed successfully");
    }

    public BaseResponse<AuthResponse> googleLogin(TokenRequest request) {
        GoogleUser user = googleTokenVerifierService.verify(request.getIdToken());
        return new BaseResponse<AuthResponse>(true, "Login successful",
                Collections.singletonList(authManager.googleLogin(user)), null);
    }

}
