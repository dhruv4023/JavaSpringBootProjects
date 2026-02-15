package com.authserver.authserver.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authserver.authserver.user.models.AccessRights;

public interface AccessRightsRepository extends JpaRepository<AccessRights, Long> {
    Optional<AccessRights> findByName(String name);
}
