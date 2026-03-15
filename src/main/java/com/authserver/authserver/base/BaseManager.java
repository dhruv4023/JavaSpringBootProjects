package com.authserver.authserver.base;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.EntityNotFoundException;

public abstract class BaseManager<ID, Entry, Entity, Repo extends JpaRepository<Entity, ID>> {

    protected final Repo repository;
    private final String entityName;

    protected BaseManager(Repo repository, String entityName) {
        this.repository = repository;
        this.entityName = entityName;
    }

    protected abstract Entity toEntity(Entry entry, Entity existing)
            throws EntityNotFoundException;

    protected abstract Entry toEntry(Entity entity) throws EntityNotFoundException;

    public Entry add(Entry entry) throws EntityNotFoundException {
        Objects.requireNonNull(entry, "Entry must not be null");
        Entity entity = toEntity(entry, null);
        Objects.requireNonNull(entity, "Entity must not be null");
        validateAddEntry(entity);
        Entity saved = repository.save(entity);
        return toEntry(saved);
    }

    protected Boolean validateAddEntry(Entity entry) {
        return true;
    }

    public Entry update(ID id, Entry entry) throws EntityNotFoundException {
        Objects.requireNonNull(id, "ID must not be null");

        Entity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityName + " not found"));
        validateUpdateEntry(entry, toEntry(existing));
        Entity updated = toEntity(entry, existing);
        Objects.requireNonNull(updated, "Updated entity must not be null");
        Entity saved = repository.save(updated);
        return toEntry(saved);
    }

    protected Boolean validateUpdateEntry(Entry newEntity, Entry existing) {
        return true;
    }

    public void delete(ID id) throws EntityNotFoundException {
        Objects.requireNonNull(id, "ID must not be null");
        repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityName + " not found"));
        repository.deleteById(id);
    }

    public Entry getById(ID id) throws EntityNotFoundException {
        Objects.requireNonNull(id, "ID must not be null");
        Entity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityName + " not found"));
        return toEntry(entity);
    }

    public Page<Entry> get(long page, long size) {
        Sort sort = getSort();
        Objects.requireNonNull(sort, "Sort must not be null");
        Pageable pageable = PageRequest.of((int) page, (int) size, sort);
        Page<Entity> entityPage = repository.findAll(pageable);
        return entityPage.map(this::toEntry);
    }

    protected Sort getSort() {
        return Sort.by("id").descending();
    }
}
