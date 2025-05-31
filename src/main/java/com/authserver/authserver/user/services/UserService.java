package com.authserver.authserver.user.services;

import com.authserver.authserver.base.helper.ResponseObject;
import com.authserver.authserver.user.entry.UserEntry;

public interface UserService {
    public ResponseObject updateUser(String username, UserEntry updatedEntry);
}
