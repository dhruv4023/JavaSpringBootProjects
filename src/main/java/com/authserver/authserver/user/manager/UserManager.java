package com.authserver.authserver.user.manager;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.base.enums.RoleEnum;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.exceptions.UserAlreadyExistsException;
import com.authserver.authserver.user.mapper.UserConvertor;
import com.authserver.authserver.user.models.RoleModel;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.user.repositories.RoleRepository;
import com.authserver.authserver.user.repositories.UserRepository;
import com.authserver.authserver.user.util.RandomPassword;

import jakarta.persistence.EntityNotFoundException;

@Component
public class UserManager extends BaseManager<Long, UserEntry, UserModel, UserRepository> {

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserConvertor userConvertor;

    UserManager(UserRepository repository, UserConvertor userConvertor,
            RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        super(repository, "user");
        this.userConvertor = userConvertor;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected UserModel toEntity(UserEntry entry, UserModel existing) throws EntityNotFoundException {
        return userConvertor.toModel(entry, existing);
    }

    @Override
    protected UserEntry toEntry(UserModel entity) throws EntityNotFoundException {
        return userConvertor.toEntry(entity);
    }

    public UserEntry findByUsername(String username) {
        return toEntry(findUserModelByUsername(username));
    }

    public UserModel findUserModelByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public UserModel update(UserModel userModel) {
        Objects.requireNonNull(userModel, "User data cannot be null");
        return repository.save(userModel);
    }

    public UserModel findUserModelByID(Long userId) {
        Objects.requireNonNull(userId, "User id cannot be null");
        return repository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public String save(UserModel user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }
        RoleModel role;
        if (repository.count() == 0) {
            role = roleRepository.findByRoleName(RoleEnum.SUPER_USER.name());
        } else {
            role = roleRepository.findByRoleName(RoleEnum.USER.name());
        }

        String password = RandomPassword.generate(5);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        repository.save(user);
        return password;
    }

}
