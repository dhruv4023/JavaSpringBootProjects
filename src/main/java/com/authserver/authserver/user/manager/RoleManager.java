package com.authserver.authserver.user.manager;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.base.ConvertorInterface;
import com.authserver.authserver.user.entry.RoleEntry;
import com.authserver.authserver.user.models.RoleModel;
import com.authserver.authserver.user.repositories.RoleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.Setter;

@Setter(onMethod = @__({ @Autowired }))
@Component
public class RoleManager extends BaseManager<Long, RoleEntry, RoleModel, RoleRepository> {

    private final ConvertorInterface<RoleEntry, RoleModel> roleConvertor;

    protected RoleManager(RoleRepository repository, ConvertorInterface<RoleEntry, RoleModel> roleConvertor) {
        super(repository, "role");
        this.roleConvertor = roleConvertor;
    }

    @Override
    protected RoleModel toEntity(RoleEntry entry, RoleModel existing) throws EntityNotFoundException {
        return roleConvertor.toModel(entry, existing);
    }

    @Override
    protected RoleEntry toEntry(RoleModel entity) throws EntityNotFoundException {
        return roleConvertor.toEntry(entity);
    }

    public RoleEntry getRoleByName(String roleName) {
        return toEntry(getRoleModelByName(roleName));
    }

    RoleModel getRoleModelByName(String roleName) {
        return repository.findByRoleName(roleName);
    }
}
