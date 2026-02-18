package com.authserver.authserver.code_note.Models;

import com.authserver.authserver.base.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "note_tags")
@NoArgsConstructor
public class TagModel extends BaseModel {

    public TagModel(String name) {
        this.name = name;
    }

    @Column(unique = true)
    private String name;
}
