package com.authserver.authserver.communication.manager;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.communication.entry.TemplateEntry;
import com.authserver.authserver.communication.models.TemplateModel;
import com.authserver.authserver.communication.repository.TemplatesRepository;
import com.authserver.authserver.user.models.UserModel;
import jakarta.persistence.EntityNotFoundException;
import lombok.Setter;

@Setter(onMethod = @__({ @Autowired }))
@Component
public class TemplateManager extends BaseManager<Long, TemplateEntry, TemplateModel, TemplatesRepository> {

    protected TemplateManager(TemplatesRepository repository) {
        super(repository, "template");
    }

    @Override
    protected TemplateModel toEntity(TemplateEntry entry, TemplateModel existing) throws EntityNotFoundException {
        TemplateModel templateModel = existing == null ? new TemplateModel() : existing;
        if (Objects.nonNull(entry.getId())) {
            templateModel.setId(entry.getId());
        }
        if (Objects.nonNull(entry.getName())) {
            templateModel.setName(entry.getName());
        }
        if (Objects.nonNull(entry.getContent())) {
            templateModel.setContent(entry.getContent());
        }
        if (Objects.nonNull(entry.getTitle())) {
            templateModel.setTitle(entry.getTitle());
        }
        if (Objects.nonNull(entry.getUserId())) {
            UserModel user = new UserModel();
            user.setId(entry.getUserId());
            templateModel.setUser(user);
        }
        return templateModel;
    }

    @Override
    protected TemplateEntry toEntry(TemplateModel entity) throws EntityNotFoundException {
        TemplateEntry templateEntry = new TemplateEntry();
        templateEntry.setId(entity.getId());
        templateEntry.setName(entity.getName());
        templateEntry.setContent(entity.getContent());
        templateEntry.setTitle(entity.getTitle());
        templateEntry.setUserId(entity.getUser().getId());
        return templateEntry;
    }

}
