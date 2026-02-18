package com.authserver.authserver.user.mapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseConvertorInterface;
import com.authserver.authserver.user.entry.RoleEntry;
import com.authserver.authserver.user.models.RoleModel;

@Component
public class RoleConvertor implements BaseConvertorInterface<RoleEntry, RoleModel>{
    
    @Override
    public RoleModel toModel(RoleEntry entry, RoleModel existing) {
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
    public RoleEntry toEntry(RoleModel entity) {
        return new RoleEntry(entity.getId(), entity.getRoleName(), entity.getDescription());
    }
}
