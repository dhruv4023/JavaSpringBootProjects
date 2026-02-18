package com.authserver.authserver.user.services.auth;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.user.entry.ChangePasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.response.AuthResponse;

public interface AuthServiceInterface {
    public BaseResponse<Void> signup(SignupEntry signupEntry);

    public BaseResponse<AuthResponse> login(LoginEntry loginEntry);

    public BaseResponse<Void> forgotPassword(String userName);
    public BaseResponse<Void> changePassword(ChangePasswordEntry forgotPasswordEntry);
}
