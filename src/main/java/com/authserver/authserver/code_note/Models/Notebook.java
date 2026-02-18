package com.authserver.authserver.code_note.Models;

import java.util.List;

import com.authserver.authserver.base.BaseModel;
import com.authserver.authserver.user.models.UserModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notebook", indexes = {
        @Index(name = "idx_notebook_name", columnList = "name")
})
@NoArgsConstructor
public class Notebook extends BaseModel {

    private String name;

    private String description;

    @OneToMany(mappedBy = "notebook", cascade = CascadeType.ALL)
    private List<CodeNote> notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    public Notebook(Long id) {
        super(id);
    }
}