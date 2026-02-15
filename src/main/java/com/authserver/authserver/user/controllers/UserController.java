package com.authserver.authserver.user.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.controllers.BaseController;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController<Long, UserEntry, UserService> {

}
