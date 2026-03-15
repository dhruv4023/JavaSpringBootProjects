package com.authserver.authserver.user.manager;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.user.repositories.UserScopedRepository;
import com.authserver.authserver.user.util.SecurityUtils;

@Component
abstract public class ResBaseManager<ID, Entry, Entity, Repo extends UserScopedRepository<Entity, ID>>
        extends BaseManager<ID, Entry, Entity, Repo> {

    protected final SecurityUtils securityutil;

    protected ResBaseManager(Repo repository, String entityName, SecurityUtils securityutil) {
        super(repository, entityName);
        this.securityutil = securityutil;
    }

    public Page<Entry> getByUserId(long page, long size) {
        Sort sort = getSort();
        Objects.requireNonNull(sort, "Sort must not be null");
        Pageable pageable = PageRequest.of((int) page, (int) size, sort);
        Long userId = securityutil.getCurrentUserId();
        Page<Entity> entityPage = repository.findByUserId(userId, pageable);
        return entityPage.map(this::toEntry);
    }

}
