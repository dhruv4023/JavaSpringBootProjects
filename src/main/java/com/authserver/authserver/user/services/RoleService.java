package com.authserver.authserver.user.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.authserver.authserver.base.service.BaseService;
import com.authserver.authserver.user.entry.RoleEntry;
import com.authserver.authserver.user.manager.RoleManager;

@Service
public class RoleService extends BaseService<UUID, RoleEntry, RoleManager> {

    public RoleService(RoleManager manager) {
        super(manager);
    }
}
