package com.authserver.authserver.user.controllers;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.controllers.BaseController;
import com.authserver.authserver.user.entry.RoleEntry;
import com.authserver.authserver.user.services.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<UUID, RoleEntry, RoleService> {

}
