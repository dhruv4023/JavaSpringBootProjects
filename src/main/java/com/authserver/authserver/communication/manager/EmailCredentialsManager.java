package com.authserver.authserver.communication.manager;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.communication.entry.EmailCredentialsEntry;
import com.authserver.authserver.communication.models.EmailCredentials;
import com.authserver.authserver.communication.repository.EmailCredentialsRepository;
import com.authserver.authserver.user.models.UserModel;

import lombok.Setter;

@Setter(onMethod = @__({ @Autowired }))
@Component
public class EmailCredentialsManager
        extends BaseManager<UUID, EmailCredentialsEntry, EmailCredentials, EmailCredentialsRepository> {

    protected EmailCredentialsManager(EmailCredentialsRepository repository) {
        super(repository, "email credentials");
    }

    @Override
    protected EmailCredentials toEntity(EmailCredentialsEntry entry, EmailCredentials existing) {
        EmailCredentials emailCredentials = existing == null ? new EmailCredentials() : existing;
        emailCredentials.setUser(new UserModel(entry.getUserUuid()));
        emailCredentials.setPasscode(entry.getPasscode());
        return emailCredentials;
    }

    @Override
    protected EmailCredentialsEntry toEntry(EmailCredentials entity) {
        EmailCredentialsEntry emailCredentialsEntry = new EmailCredentialsEntry();
        if (Objects.nonNull(entity.getUser().getUuid())) {
            emailCredentialsEntry.setUserUuid(entity.getUser().getUuid());
        }
        if (Objects.nonNull(entity.getPasscode())) {
            emailCredentialsEntry.setPasscode(entity.getPasscode());
        }
        return emailCredentialsEntry;
    }

}
