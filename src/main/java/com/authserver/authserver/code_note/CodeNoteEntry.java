package com.authserver.authserver.code_note;

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
    private String aiDescription;
    private Long userId;
}
