package com.authserver.authserver.code_note.managers;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.code_note.Models.TagModel;
import com.authserver.authserver.code_note.convertor.TagConvertor;
import com.authserver.authserver.code_note.entry.TagEntry;
import com.authserver.authserver.code_note.repository.TagRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class TagManager extends BaseManager<Long, TagEntry, TagModel, TagRepository> {

    private final TagConvertor tagConvertor;

    protected TagManager(TagRepository repository, TagConvertor tagConvertor) {
        super(repository, "Tags");
        this.tagConvertor = tagConvertor;
    }

    @Override
    protected TagModel toEntity(TagEntry entry, TagModel existing) throws EntityNotFoundException {
        return tagConvertor.toModel(entry, existing);

    }

    @Override
    protected TagEntry toEntry(TagModel entity) throws EntityNotFoundException {
        return tagConvertor.toEntry(entity);
    }

    public TagModel findTagModelByName(String name) {
        return repository.findAll().stream().filter(tag -> tag.getName().equals(name.toUpperCase())).findFirst().orElse(null);
    }

    public TagModel addTag(String name) {
        if (Objects.nonNull(findTagModelByName(name))) {
            return findTagModelByName(name);
        }
        TagModel tag = new TagModel(name.toUpperCase());
        return repository.save(tag);
    }

}
