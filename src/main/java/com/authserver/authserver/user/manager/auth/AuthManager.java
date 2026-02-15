package com.authserver.authserver.user.manager.auth;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.authserver.authserver.base.enums.RoleEnum;
import com.authserver.authserver.user.entry.ForgotPasswordEntry;
import com.authserver.authserver.user.entry.LoginEntry;
import com.authserver.authserver.user.entry.RoleEntry;
import com.authserver.authserver.user.entry.SignupEntry;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.exceptions.InvalidPasswordException;
import com.authserver.authserver.user.exceptions.UserAlreadyExistsException;
import com.authserver.authserver.user.exceptions.UserNotFoundException;
import com.authserver.authserver.user.exceptions.ValidationException;
import com.authserver.authserver.user.manager.RoleManager;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.user.repositories.UserRepository;
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
    private RoleManager roleManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginEntry loginEntry) {
        UserModel user = userRepository.findByUsername(loginEntry.getUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        "User with username " + loginEntry.getUsername() + " not found"));
        if (passwordEncoder.matches(loginEntry.getPassword(), user.getPassword())) {
            UserEntry userEntry = UserEntry.fromModel(user);
            String token = jwtUtil.generateToken(user.getId(), loginEntry.getUsername(),
                    user.getRole().getRoleName());
            session.setAttribute("user", loginEntry.getUsername());
            return new AuthResponse(userEntry, token);
        } else {
            throw new InvalidPasswordException("Invalid password for username " + loginEntry.getUsername());
        }
    }

    public UserModel convertToUserModel(SignupEntry signupEntry) {
        RoleEntry role = roleManager.getRoleByName(signupEntry.getRoleName());
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
        user.setRole(role.toModel());
        return user;
    }

    @Override
    public void forgotPassword(ForgotPasswordEntry forgotPasswordEntry) {
        UserModel user = userRepository.findByUsername(forgotPasswordEntry.getUsername())
                .orElseThrow(() -> new UserNotFoundException(
                        "User with username " + forgotPasswordEntry.getUsername() + " not found"));
        user.setPassword(passwordEncoder.encode(forgotPasswordEntry.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void signup(SignupEntry signupEntry) {
        userRepository.findByUsername(signupEntry.getUsername())
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException(
                            "User with username " + signupEntry.getUsername() + " already exists");
                });

        if (!userRepository.existsBy()) {
            signupEntry.setRoleName(RoleEnum.SUPER_USER.name());
        } else {
            signupEntry.setRoleName(RoleEnum.USER.name());
        }

        userRepository.save(convertToUserModel(signupEntry));
    }
}
