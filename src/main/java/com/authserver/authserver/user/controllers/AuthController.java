package com.authserver.authserver.user.controllers;

import com.authserver.authserver.base.helper.ResponseObject;
import com.authserver.authserver.user.entry.ForgotPasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseObject> signup(@RequestBody SignupEntry signupEntry) {
        ResponseObject response = userService.signup(signupEntry);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@RequestBody LoginEntry loginEntry) {
        ResponseObject response = userService.login(loginEntry);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseObject> forgotPassword(@RequestBody ForgotPasswordEntry forgotPasswordEntry) {
        return ResponseEntity.ok(userService.forgotPassword(forgotPasswordEntry));
    }
}
