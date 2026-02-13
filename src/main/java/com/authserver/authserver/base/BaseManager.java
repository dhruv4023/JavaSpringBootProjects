package com.authserver.authserver.base;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.EntityNotFoundException;

public abstract class BaseManager<Entity, ID, Entry> {

    private final JpaRepository<Entity, ID> repository;
    private final String entityName;

    protected BaseManager(JpaRepository<Entity, ID> repository, String entityName) {
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
        Entity saved = repository.save(entity);
        return toEntry(saved);
    }

    public Entry update(ID id, Entry entry) throws EntityNotFoundException {
        Objects.requireNonNull(id, "ID must not be null");

        Entity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(entityName + " not found"));

        Entity updated = toEntity(entry, existing);
        Objects.requireNonNull(updated, "Updated entity must not be null");
        Entity saved = repository.save(updated);
        return toEntry(saved);
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
        Pageable pageable = PageRequest.of((int) page, (int) size);
        Page<Entity> entityPage = repository.findAll(pageable);
        return entityPage.map(this::toEntry);
    }
}
