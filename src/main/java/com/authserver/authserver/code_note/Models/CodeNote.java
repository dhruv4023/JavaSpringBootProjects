package com.authserver.authserver.code_note.Models;

import java.util.HashSet;
import java.util.Set;

import com.authserver.authserver.base.BaseModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "code_notes",
    indexes = {
        @Index(name = "idx_codenote_position_notebook", columnList = "notebook_id,position")
    }
)
public class CodeNote extends BaseModel {
    private String permanentLink;
    private String note;
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String aiSummary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String aiExplanation;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String aiDescription;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String aiImprovements;
    private String aiEmbeddingId;

    @Column(nullable = false)
    private Long position;

    @ManyToMany
    private Set<TagModel> aiTags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notebook_id", nullable = false)
    private Notebook notebook;
}
