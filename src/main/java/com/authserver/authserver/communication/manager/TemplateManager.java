package com.authserver.authserver.communication.manager;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.communication.entry.TemplateEntry;
import com.authserver.authserver.communication.models.TemplateModel;
import com.authserver.authserver.communication.repository.TemplatesRepository;
import com.authserver.authserver.user.models.UserModel;
import lombok.Setter;

@Setter(onMethod = @__({ @Autowired }))
@Component
public class TemplateManager extends BaseManager<UUID, TemplateEntry, TemplateModel, TemplatesRepository> {

    protected TemplateManager(TemplatesRepository repository) {
        super(repository, "template");
    }

    @Override
    protected TemplateModel toEntity(TemplateEntry entry, TemplateModel existing) {
        TemplateModel templateModel = existing == null ? new TemplateModel() : existing;
        templateModel.setUuid(entry.getUuid());
        if (Objects.nonNull(entry.getName())) {
            templateModel.setName(entry.getName());
        }
        if (Objects.nonNull(entry.getContent())) {
            templateModel.setContent(entry.getContent());
        }
        if (Objects.nonNull(entry.getTitle())) {
            templateModel.setTitle(entry.getTitle());
        }
        if (Objects.nonNull(entry.getUserUuid())) {
            UserModel user = new UserModel();
            user.setUuid(entry.getUserUuid());
            templateModel.setUser(user);
        }
        return templateModel;
    }

    @Override
    protected TemplateEntry toEntry(TemplateModel entity) {
        TemplateEntry templateEntry = new TemplateEntry();
        templateEntry.setUuid(entity.getUuid());
        templateEntry.setName(entity.getName());
        templateEntry.setContent(entity.getContent());
        templateEntry.setTitle(entity.getTitle());
        templateEntry.setUserUuid(entity.getUser().getUuid());
        return templateEntry;
    }

}
