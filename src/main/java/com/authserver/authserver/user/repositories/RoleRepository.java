package com.authserver.authserver.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authserver.authserver.user.models.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    RoleModel findByRoleName(String roleName);
}
