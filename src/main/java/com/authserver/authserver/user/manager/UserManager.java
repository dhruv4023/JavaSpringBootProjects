package com.authserver.authserver.user.manager;

import org.springframework.stereotype.Component;
import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.base.enums.RoleEnum;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.mapper.UserConvertor;
import com.authserver.authserver.user.models.RoleModel;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.user.repositories.RoleRepository;
import com.authserver.authserver.user.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class UserManager extends BaseManager<Long, UserEntry, UserModel, UserRepository> {

    private final RoleRepository roleRepository;

    private final UserConvertor userConvertor;

    UserManager(UserRepository repository, UserConvertor userConvertor,
            RoleRepository roleRepository) {
        super(repository, "user");
        this.userConvertor = userConvertor;
        this.roleRepository = roleRepository;
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

    public void save(UserModel user) {

        RoleModel role;

        if (repository.count() == 0) {
            role = roleRepository.findByRoleName(RoleEnum.SUPER_USER.name());
        } else {
            role = roleRepository.findByRoleName(RoleEnum.USER.name());
        }

        user.setRole(role);
        repository.save(user);
    }

}
