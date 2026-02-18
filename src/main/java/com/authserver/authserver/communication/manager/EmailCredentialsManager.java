package com.authserver.authserver.communication.manager;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.communication.entry.EmailCredentialsEntry;
import com.authserver.authserver.communication.models.EmailCredentials;
import com.authserver.authserver.communication.repository.EmailCredentialsRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.Setter;

@Setter(onMethod = @__({ @Autowired }))
@Component
public class EmailCredentialsManager
        extends BaseManager<Long, EmailCredentialsEntry, EmailCredentials, EmailCredentialsRepository> {

    protected EmailCredentialsManager(EmailCredentialsRepository repository) {
        super(repository, "email credentials");
    }

    @Override
    protected EmailCredentials toEntity(EmailCredentialsEntry entry, EmailCredentials existing)
            throws EntityNotFoundException {
        EmailCredentials emailCredentials = existing == null ? new EmailCredentials() : existing;
        emailCredentials.setId(entry.getUserId());
        emailCredentials.setPasscode(entry.getPasscode());
        return emailCredentials;
    }

    @Override
    protected EmailCredentialsEntry toEntry(EmailCredentials entity) throws EntityNotFoundException {
        EmailCredentialsEntry emailCredentialsEntry = new EmailCredentialsEntry();
        if (Objects.nonNull(entity.getUser().getId())) {
            emailCredentialsEntry.setUserId(entity.getUser().getId());
        }
        if (Objects.nonNull(entity.getPasscode())) {
            emailCredentialsEntry.setPasscode(entity.getPasscode());
        }
        return emailCredentialsEntry;
    }

}
