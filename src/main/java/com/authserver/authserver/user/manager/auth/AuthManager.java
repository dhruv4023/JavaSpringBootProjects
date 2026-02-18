package com.authserver.authserver.user.manager.auth;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.authserver.authserver.user.entry.ForgotPasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.exceptions.InvalidPasswordException;
import com.authserver.authserver.user.manager.UserManager;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.user.response.AuthResponse;
import com.authserver.authserver.user.security.JwtService;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Setter(onMethod = @__({ @Autowired }))
@Component
public class AuthManager implements AuthManagerInterface {

    @Autowired
    private JwtService jwtUtil;

    @Autowired
    private HttpSession session;

    @Autowired
    private UserManager userManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginEntry loginEntry) {
        UserEntry userEntry = userManager.findByUsername(loginEntry.getUsername());
        UserModel user = userManager.findUserModelByUsername(loginEntry.getUsername());
        if (passwordEncoder.matches(loginEntry.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getId(), loginEntry.getUsername(),
                    user.getRole().getRoleName());
            session.setAttribute("user", loginEntry.getUsername());
            return new AuthResponse(userEntry, token);
        } else {
            throw new InvalidPasswordException("Invalid password for username " + loginEntry.getUsername());
        }
    }

    public UserModel convertToUserModel(SignupEntry signupEntry) {
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
        return user;
    }

    @Override
    public void forgotPassword(ForgotPasswordEntry forgotPasswordEntry) {
        UserModel user = userManager.findUserModelByUsername(forgotPasswordEntry.getUsername());
        user.setPassword(passwordEncoder.encode(forgotPasswordEntry.getNewPassword()));
        userManager.save(user);
    }

    @Override
    public void signup(SignupEntry signupEntry) {
        userManager.save(convertToUserModel(signupEntry));
    }
}
