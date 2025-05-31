package com.authserver.authserver.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.helper.ResponseObject;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.security.AuthUserDetails;
import com.authserver.authserver.user.services.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateUser(@RequestBody UserEntry userEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        return ResponseEntity.ok(userService.updateUser(username, userEntry));
    }
}
