package com.authserver.authserver.user.services;

import org.springframework.stereotype.Service;

import com.authserver.authserver.base.service.BaseService;
import com.authserver.authserver.user.entry.RoleEntry;
import com.authserver.authserver.user.manager.RoleManager;

@Service
public class RoleService extends BaseService<Long, RoleEntry, RoleManager> {

    public RoleService(RoleManager manager) {
        super(manager);
    }
}
