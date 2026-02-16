package com.authserver.authserver.communication.manager;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.communication.entry.TemplateEntry;
import com.authserver.authserver.communication.models.TemplateModel;
import com.authserver.authserver.communication.repository.TemplatesRepository;

import jakarta.persistence.EntityNotFoundException;

public class TemplateManager extends BaseManager<Long, TemplateEntry, TemplateModel, TemplatesRepository> {

    protected TemplateManager(TemplatesRepository repository) {
        super(repository, "template");
    }

    @Override
    protected TemplateModel toEntity(TemplateEntry entry, TemplateModel existing) throws EntityNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toEntity'");
    }

    @Override
    protected TemplateEntry toEntry(TemplateModel entity) throws EntityNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toEntry'");
    }
    
}
