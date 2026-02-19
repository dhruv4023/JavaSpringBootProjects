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
    private String permanentLink;
    private String note;
    private String title;
    private String aiSummary;
    private String aiExplanation;
    private String aiImprovements;
    private String aiEmbeddingId;
    private Set<String> aiTags;

    private Long userId;
}
