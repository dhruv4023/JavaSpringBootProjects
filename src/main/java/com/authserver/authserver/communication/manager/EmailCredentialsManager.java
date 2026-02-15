package com.authserver.authserver.communication.manager;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.communication.entry.EmailCredentialsEntry;
import com.authserver.authserver.communication.models.EmailCredentials;
import com.authserver.authserver.communication.repository.EmailCredentialsRepository;

import jakarta.persistence.EntityNotFoundException;


public class EmailCredentialsManager extends BaseManager<Long, EmailCredentialsEntry, EmailCredentials, EmailCredentialsRepository  >{

    protected EmailCredentialsManager(EmailCredentialsRepository repository) {
        super(repository, "email credentials");
    }

    @Override
    protected EmailCredentials toEntity(EmailCredentialsEntry entry, EmailCredentials existing)
            throws EntityNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toEntity'");
    }

    @Override
    protected EmailCredentialsEntry toEntry(EmailCredentials entity) throws EntityNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toEntry'");
    }
    
}
