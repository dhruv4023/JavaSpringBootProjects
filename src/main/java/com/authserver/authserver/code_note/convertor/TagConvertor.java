package com.authserver.authserver.code_note.convertor;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.authserver.authserver.base.BaseConvertorInterface;
import com.authserver.authserver.code_note.Models.TagModel;
import com.authserver.authserver.code_note.entry.TagEntry;

@Component
public class TagConvertor implements BaseConvertorInterface<TagEntry, TagModel> {

    @Override
    public TagModel toModel(TagEntry entry, TagModel existing) {
        TagModel model = existing == null ? new TagModel() : existing;
        if (Objects.nonNull(entry.getId()))
            model.setId(entry.getId());
        if (Objects.nonNull(entry.getName())) {
            model.setName(entry.getName());
        }
        return model;
    }

    @Override
    public TagEntry toEntry(TagModel model) {
        return new TagEntry(
                model.getId(),
                model.getName());
    }

}
