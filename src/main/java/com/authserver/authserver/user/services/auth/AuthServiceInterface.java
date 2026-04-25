package com.authserver.authserver.user.services.auth;

import org.springframework.http.ResponseEntity;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.user.entry.ChangePasswordEntry;
import com.authserver.authserver.user.entry.ForgotPasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.response.AuthResponse;

public interface AuthServiceInterface {
    public ResponseEntity<BaseResponse<Void>> signup(SignupEntry signupEntry);

    public ResponseEntity<BaseResponse<AuthResponse>> login(LoginEntry loginEntry);

    public ResponseEntity<BaseResponse<Void>> forgotPassword(ForgotPasswordEntry forgotPasswordEntry);

    public ResponseEntity<BaseResponse<Void>> changePassword(ChangePasswordEntry forgotPasswordEntry);
}
