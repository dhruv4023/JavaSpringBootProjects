package com.authserver.authserver.user.manager;

import java.util.Objects;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.user.entry.RoleEntry;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.user.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.Setter;

@Setter(onMethod = @__({ @Autowired }))
@Component
public class UserManager extends BaseManager<Long, UserEntry, UserModel, UserRepository> {

    protected UserManager(UserRepository repository) {
        super(repository, "user");
    }

    @Override
    protected UserModel toEntity(UserEntry entry, UserModel existing) throws EntityNotFoundException {
        UserModel user = existing != null ? existing : new UserModel();
        if (Objects.nonNull(entry.getUsername())) {
            user.setUsername(entry.getUsername());
        }
        if (Objects.nonNull(entry.getEmail())) {
            user.setEmail(entry.getEmail());
        }
        return user;
    }

    @Override
    protected UserEntry toEntry(UserModel entity) throws EntityNotFoundException {
        UserEntry entry = new UserEntry();
        entry.setId(entity.getId());
        entry.setUsername(entity.getUsername());
        entry.setEmail(entity.getEmail());
        entry.setRoleEntry(RoleEntry.fromModel(entity.getRole()));
        return entry;
    }

}
