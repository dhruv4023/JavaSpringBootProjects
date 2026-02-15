package com.authserver.authserver.user.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.controllers.BaseController;
import com.authserver.authserver.user.entry.RoleEntry;
import com.authserver.authserver.user.services.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<Long, RoleEntry, RoleService> {

}
