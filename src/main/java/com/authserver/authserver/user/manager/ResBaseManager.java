package com.authserver.authserver.user.manager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<Entry> getBySpec(long page, long size, String search) {
        Pageable pageable = PageRequest.of((int) page, (int) size, getSort());
        Long userId = securityutil.getCurrentUserId();
        Specification<Entity> spec = buildSpecification(search, userId);
        Page<Entity> entityPage = repository.findAll(spec, pageable);
        return entityPage.map(this::toEntry);
    }

    protected Specification<Entity> buildSpecification(String search, Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }
}
