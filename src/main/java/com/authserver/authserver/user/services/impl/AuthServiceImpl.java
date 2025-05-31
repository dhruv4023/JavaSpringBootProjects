package com.authserver.authserver.user.services.impl;

import com.authserver.authserver.base.helper.ResponseObject;
import com.authserver.authserver.user.entry.ForgotPasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.exceptions.*;
import com.authserver.authserver.user.lang.En;
import com.authserver.authserver.user.models.RoleModel;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.user.repositories.UserRepository;
import com.authserver.authserver.user.response.AuthResponse;
import com.authserver.authserver.user.security.JwtService;
import com.authserver.authserver.user.services.AuthService;
import com.authserver.authserver.user.services.RoleService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtService jwtUtil;

    @Autowired
    private HttpSession session;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseObject signup(SignupEntry signupEntry) {
        userRepository.save(convertToUserModel(signupEntry));
        return new ResponseObject(true, "User registered successfully", null);

    }

    public ResponseObject login(LoginEntry loginEntry) {
        UserModel user = userRepository.findByUsername(loginEntry.getUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        "User with username " + loginEntry.getUsername() + " not found"));
        if (passwordEncoder.matches(loginEntry.getPassword(), user.getPassword())) {
            UserEntry userEntry = UserEntry.fromModel(user);
            String token = jwtUtil.generateToken(user.getId(), loginEntry.getUsername(),
                    user.getRole().getRoleName());
            session.setAttribute("user", loginEntry.getUsername());
            return new ResponseObject(true, "Login successful", new AuthResponse(userEntry, token));
        } else {
            throw new InvalidPasswordException("Invalid password for username " + loginEntry.getUsername());
        }
    }

    public UserModel convertToUserModel(SignupEntry signupEntry) {
        RoleModel role = roleService.getRoleByName(signupEntry.getRoleName());
        if (role == null) {
            throw new ValidationException("Role " + signupEntry.getRoleName() + " does not exist.");
        }
        UserModel user = new UserModel();
        if (Objects.nonNull(signupEntry.getUsername())) {
            user.setUsername(signupEntry.getUsername());
        }
        if (Objects.nonNull(signupEntry.getEmail())) {
            user.setEmail(signupEntry.getEmail());
        }
        if (Objects.nonNull(signupEntry.getPassword())) {
            user.setPassword(passwordEncoder.encode(signupEntry.getPassword()));
        }
        user.setRole(role);

        return user;
    }

    @Override
    public ResponseObject forgotPassword(ForgotPasswordEntry forgotPasswordEntry) {
        UserModel user = userRepository.findByUsername(forgotPasswordEntry.getUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        "User with username " + forgotPasswordEntry.getUsername() + " not found"));
        user.setPassword(passwordEncoder.encode(forgotPasswordEntry.getNewPassword()));
        userRepository.save(user);

        return new ResponseObject(false, En.FORGOT_PASSWORD_SUCCESS, null);
    }
}
