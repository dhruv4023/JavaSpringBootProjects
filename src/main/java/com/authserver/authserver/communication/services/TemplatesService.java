package com.authserver.authserver.communication.services;

import org.springframework.stereotype.Service;

import com.authserver.authserver.base.service.BaseService;
import com.authserver.authserver.communication.entry.TemplateEntry;
import com.authserver.authserver.communication.manager.TemplateManager;

@Service
public class TemplatesService extends BaseService<Long, TemplateEntry, TemplateManager> {

    public TemplatesService(TemplateManager manager) {
        super(manager);
    }

}
