package com.authserver.authserver.code_note.entry;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotebookEntry {
    private Long id;
    private String name;
    private String description;
    private List<CodeNoteEntry> notes;
    private Long userId;
}
