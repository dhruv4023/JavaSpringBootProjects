package com.authserver.authserver.user.services.auth;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.base.response.ResponseBuilder;
import com.authserver.authserver.google_auth.GoogleTokenVerifierService;
import com.authserver.authserver.google_auth.GoogleUser;
import com.authserver.authserver.google_auth.TokenRequest;
import com.authserver.authserver.user.entry.ChangePasswordEntry;
import com.authserver.authserver.user.entry.ForgotPasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.manager.auth.AuthManagerInterface;
import com.authserver.authserver.user.response.AuthResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthServiceInterface {

    AuthManagerInterface authManager;
    private final GoogleTokenVerifierService googleTokenVerifierService;

    public AuthService(AuthManagerInterface authManager, GoogleTokenVerifierService googleTokenVerifierService) {
        this.authManager = authManager;
        this.googleTokenVerifierService = googleTokenVerifierService;
    }

    @Override
    public ResponseEntity<BaseResponse<Void>> signup(SignupEntry signupEntry) {
        authManager.signup(signupEntry);
        return ResponseBuilder.single(null, "User registered successfully", null);
    }

    @Override
    public ResponseEntity<BaseResponse<AuthResponse>> login(LoginEntry loginEntry) {
        return ResponseBuilder.single(() -> authManager.login(loginEntry), "Login successful", null);
    }

    @Override
    public ResponseEntity<BaseResponse<Void>> forgotPassword(ForgotPasswordEntry forgotPasswordEntry) {
        authManager.forgotPassword(forgotPasswordEntry);
        return ResponseBuilder.single(null,
                "We received your request to reset your password. Please check your email in few minutes.", null);
    }

    @Override
    public ResponseEntity<BaseResponse<Void>> changePassword(ChangePasswordEntry changePasswordEntry) {
        authManager.changePassword(changePasswordEntry);
        return ResponseBuilder.single(null, "Password changed successfully", null);
    }

    public ResponseEntity<BaseResponse<AuthResponse>> googleLogin(TokenRequest request) {
        GoogleUser user = googleTokenVerifierService.verify(request.getIdToken());
        return ResponseBuilder.single(() -> authManager.googleLogin(user), "Login successful", null);
    }

}
