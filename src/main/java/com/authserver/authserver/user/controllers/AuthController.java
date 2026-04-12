package com.authserver.authserver.user.controllers;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.user.entry.ChangePasswordEntry;
import com.authserver.authserver.user.entry.ForgotPasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.response.AuthResponse;
import com.authserver.authserver.user.services.auth.AuthService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<Void>> signup(@Valid @RequestBody SignupEntry signupEntry) {
        BaseResponse<Void> response = authService.signup(signupEntry);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(@RequestBody LoginEntry loginEntry) {
        BaseResponse<AuthResponse> response = authService.login(loginEntry);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<BaseResponse<Void>> forgotPassword(@RequestBody ForgotPasswordEntry forgotPasswordEntry) {
        return ResponseEntity.ok(authService.forgotPassword(forgotPasswordEntry));
    }

    @PostMapping("/change-password")
    public ResponseEntity<BaseResponse<Void>> changerPassword(@RequestBody ChangePasswordEntry forgotPasswordEntry) {
        return ResponseEntity.ok(authService.changePassword(forgotPasswordEntry));
    }

}
