package com.authserver.authserver.user.repositories;

import java.util.UUID;

import com.authserver.authserver.base.BaseRepository;

import com.authserver.authserver.user.models.RoleModel;

public interface RoleRepository extends BaseRepository<RoleModel, UUID> {
    RoleModel findByRoleName(String roleName);
}
