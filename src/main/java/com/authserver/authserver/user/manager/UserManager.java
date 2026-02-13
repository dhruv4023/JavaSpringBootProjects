package com.authserver.authserver.user.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.models.UserModel;
import com.authserver.authserver.user.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.Setter;

@Setter(onMethod = @__({ @Autowired }))
@Component
public class UserManager extends BaseManager<UserModel, Long, UserEntry> {

    protected UserManager(UserRepository repository) {
        super(repository, "user");
    }

    @Override
    protected UserModel toEntity(UserEntry entry, UserModel existing) throws EntityNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toEntity'");
    }

    @Override
    protected UserEntry toEntry(UserModel entity) throws EntityNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toEntry'");
    }

}
