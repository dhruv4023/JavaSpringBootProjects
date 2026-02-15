package com.authserver.authserver.user.manager;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.user.entry.RoleEntry;
import com.authserver.authserver.user.models.RoleModel;
import com.authserver.authserver.user.repositories.RoleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.Setter;

@Setter(onMethod = @__({ @Autowired }))
@Component
public class RoleManager extends BaseManager<Long, RoleEntry, RoleModel, RoleRepository> {

    protected RoleManager(RoleRepository repository) {
        super(repository, "role");
    }

    @Override
    protected RoleModel toEntity(RoleEntry entry, RoleModel existing) throws EntityNotFoundException {
        RoleModel role = existing != null ? existing : new RoleModel();
        if (Objects.nonNull(entry.getRoleName())) {
            role.setRoleName(entry.getRoleName());
        }
        if (Objects.nonNull(entry.getDescription())) {
            role.setDescription(entry.getDescription());
        }
        return role;
    }

    @Override
    protected RoleEntry toEntry(RoleModel entity) throws EntityNotFoundException {
        return RoleEntry.fromModel(entity);
    }

    public RoleEntry getRoleByName(String roleName) {
        return toEntry(repository.findByRoleName(roleName));
    }
}
