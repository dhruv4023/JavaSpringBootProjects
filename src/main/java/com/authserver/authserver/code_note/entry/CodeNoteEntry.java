package com.authserver.authserver.code_note.entry;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeNoteEntry {
    private Long id;
    private Long notebookId;
    private String permanentLink;
    private String description;
    private String title;

    private Long position;
    private String aiSummary;
    private String aiExplanation;
    private String aiDescription;
    private String aiImprovements;
    private String aiEmbeddingId;
    private Set<String> aiTags;
}
