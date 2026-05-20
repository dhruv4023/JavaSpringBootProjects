package com.authserver.authserver.user.repositories;

import com.authserver.authserver.base.BaseRepository;

import com.authserver.authserver.user.models.RoleModel;

public interface RoleRepository extends BaseRepository<RoleModel, Long> {
    RoleModel findByRoleName(String roleName);
}
