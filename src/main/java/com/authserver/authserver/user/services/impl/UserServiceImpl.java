package com.authserver.authserver.user.services.impl;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.authserver.authserver.user.models.RoleModel;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.base.helper.ResponseObject;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.repositories.UserRepository;
import com.authserver.authserver.user.services.RoleService;
import com.authserver.authserver.user.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseObject updateUser(String username, UserEntry updatedEntry) {
        try {
            Optional<UserModel> optionalUser = userRepository.findByUsername(username);

            if (optionalUser.isEmpty()) {
                throw new RuntimeException("User not found with ID: " + username);
            }

            UserModel existingUser = optionalUser.get();

            if (Objects.nonNull(updatedEntry.getUsername())) {
                existingUser.setUsername(updatedEntry.getUsername());
            }
            if (Objects.nonNull(updatedEntry.getEmail())) {
                existingUser.setEmail(updatedEntry.getEmail());
            }
            if (Objects.nonNull(updatedEntry.getRoleEntry())) {
                RoleModel role = roleService.getRoleByName(updatedEntry.getRoleEntry().getRoleName());
                existingUser.setRole(role);
            }

            UserModel savedUser = userRepository.save(existingUser);

            return new ResponseObject(true, "Login successful", UserEntry.fromModel(savedUser));

        } catch (Exception e) {
            return new ResponseObject(false, e.getMessage(), null);
        }
    }
}
