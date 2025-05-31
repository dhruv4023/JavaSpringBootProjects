package com.authserver.authserver.user.services;

import java.util.List;
import java.util.Optional;

import com.authserver.authserver.user.models.RoleModel;

public interface RoleService {
    public RoleModel createRole(RoleModel roleModel);

    public RoleModel getRoleByName(String roleName);

    public List<RoleModel> getAllRoles();

    public Optional<RoleModel> getRoleById(Long id);

    public RoleModel updateRole(Long id, RoleModel updatedRole);

    public Boolean deleteRole(Long id);
}
