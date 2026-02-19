package com.authserver.authserver.code_note.Models;

import java.util.Set;

import com.authserver.authserver.base.BaseModel;
import com.authserver.authserver.user.models.UserModel;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "code_notes")
public class CodeNoteModel extends BaseModel {
    private String permanentLink;
    private String note;
    private String title;
    private String aiSummary;
    private String aiExplanation;
    private String aiImprovements;
    private String aiEmbeddingId;

    @ManyToMany
    private Set<TagModel> aiTags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;
}
