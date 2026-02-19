package com.authserver.authserver.user.manager.auth;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.authserver.authserver.communication.services.EmailService;
import com.authserver.authserver.user.entry.ChangePasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.exceptions.InvalidPasswordException;
import com.authserver.authserver.user.manager.UserManager;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.user.response.AuthResponse;
import com.authserver.authserver.user.security.JwtService;
import com.authserver.authserver.user.util.RandomPassword;
import com.authserver.authserver.user.util.SecurityUtils;

import jakarta.servlet.http.HttpSession;

@Component
public class AuthManager implements AuthManagerInterface {

    private final EmailService emailService;

    @Autowired
    private JwtService jwtUtil;

    @Autowired
    private HttpSession session;

    @Autowired
    private UserManager userManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityUtils securityUtil;

    AuthManager(EmailService emailService) {
        this.emailService = emailService;
    }

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
        return user;
    }

    @Override
    public void changePassword(ChangePasswordEntry changePasswordEntry) {
        String username = (String) securityUtil.getCurrentUsername();
        UserModel user = userManager.findUserModelByUsername(username);
        user.setPassword(passwordEncoder.encode(changePasswordEntry.getNewPassword()));
        userManager.save(user);
    }

    @Override
    @Transactional
    public void signup(SignupEntry signupEntry) {

        UserModel user = convertToUserModel(signupEntry);
        String password = userManager.save(user);

        emailService.sendEmail(
                user.getEmail(),
                "Welcome to Auth Server 🎉",
                """
                        Hello %s,

                        Your account has been created successfully.

                        You can now login using your registered email.

                        Your password is: %s

                        If you did not create this account, please contact support immediately.

                        Regards,
                        Auth Server Team
                        """.formatted(user.getUsername(), password));
    }

    @Override
    public void forgotPassword(String userName) {
        UserEntry userEntry = userManager.findByUsername(userName);
        String newPassword = RandomPassword.generate(5);
        emailService.sendEmail(
                userEntry.getEmail(),
                "Your New Password",
                """
                        Hello %s,

                        Your password has been reset successfully.

                        Your new temporary password is:

                        %s

                        Please login and change your password immediately.

                        Regards,
                        Auth Server Team
                        """.formatted(userEntry.getUsername(), newPassword));
    }
}
