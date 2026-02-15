package com.authserver.authserver.communication.services;

import com.authserver.authserver.base.service.BaseService;
import com.authserver.authserver.communication.entry.EmailCredentialsEntry;
import com.authserver.authserver.communication.manager.EmailCredentialsManager;

public class EmailCredentialsService extends BaseService<Long, EmailCredentialsEntry, EmailCredentialsManager> {

    public EmailCredentialsService(EmailCredentialsManager manager) {
        super(manager);
    }

}
