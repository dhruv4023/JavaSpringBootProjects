package com.authserver.authserver.user.repositories;

import java.util.Optional;
import java.util.UUID;

import com.authserver.authserver.base.BaseRepository;

import com.authserver.authserver.user.models.AccessRights;
    
public interface AccessRightsRepository extends BaseRepository<AccessRights, UUID> {
    Optional<AccessRights> findByName(String name);
}
