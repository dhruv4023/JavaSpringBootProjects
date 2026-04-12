package com.authserver.authserver.user.manager.auth;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.authserver.authserver.event_queue.EventQueueEntry;
import com.authserver.authserver.event_queue.QueueHandlerInterface;
import com.authserver.authserver.user.entry.ChangePasswordEntry;
import com.authserver.authserver.user.entry.ForgotPasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.events.ProducerEmailEvent;
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

    private final ObjectMapper objectMapper;

    private final List<QueueHandlerInterface> handlers;

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

    AuthManager(ObjectMapper objectMapper, List<QueueHandlerInterface> handlers) {
        this.objectMapper = objectMapper;
        this.handlers = handlers;
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

    @Transactional
    @Override
    public void changePassword(ChangePasswordEntry changePasswordEntry) {
        String username = (String) securityUtil.getCurrentUsername();
        UserModel user = userManager.findUserModelByUsername(username);
        user.setPassword(passwordEncoder.encode(changePasswordEntry.getNewPassword()));
        userManager.update(user);
    }

    @Override
    @Transactional
    public void signup(SignupEntry signupEntry) {

        UserModel user = convertToUserModel(signupEntry);
        String password = userManager.save(user);

        ProducerEmailEvent event = new ProducerEmailEvent();
        event.setUserId(user.getId());
        event.setToEmail(user.getEmail());
        event.setSubject("Welcome to Auth Server 🎉");
        event.setBody("""
                Hello %s,

                Your account has been created successfully.

                You can now login using your registered email.

                Your password is: %s

                If you did not create this account, please contact support immediately.

                Regards,
                Auth Server Team
                """.formatted(user.getUsername(), password));
        try {
            getHandler("user.created")
                    .addToQueue(new EventQueueEntry(null, objectMapper.writeValueAsString(event)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to send forgot password email");
        }
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordEntry forgotPasswordEntry) {
        UserModel user = userManager.findUserModelByUsername(forgotPasswordEntry.getUsername());
        String newPassword = RandomPassword.generate(5);
        user.setPassword(passwordEncoder.encode(newPassword));
        userManager.update(user);

        ProducerEmailEvent event = new ProducerEmailEvent();
        event.setUserId(user.getId());
        event.setToEmail(user.getEmail());
        event.setSubject("Your New Password");
        event.setBody("""
                Hello %s,

                Your password has been reset successfully.

                Your new temporary password is:

                %s

                Please login and change your password immediately.

                If you did not request a password reset, please contact support immediately.
                Regards,
                Auth Server Team
                """.formatted(user.getUsername(), newPassword));
        try {
            getHandler("user.forgot.password")
                    .addToQueue(new EventQueueEntry(null, objectMapper.writeValueAsString(event)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send forgot password email");
        }
    }

    private QueueHandlerInterface getHandler(String eventType) {
        return handlers.stream()
                .filter(h -> h.eventType().equals(eventType))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No handler for event: " + eventType));
    }
}
