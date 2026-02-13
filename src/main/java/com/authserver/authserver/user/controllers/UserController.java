package com.authserver.authserver.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.controllers.BaseController;
import com.authserver.authserver.base.service.BaseService;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController<UserEntry, Long> {

    @Autowired
    private UserService userService;

    @Override
    protected BaseService<UserEntry, Long> getService() {
        return userService;
    }
}
