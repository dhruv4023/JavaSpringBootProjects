package com.authserver.authserver.code_note.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPrCodeNoteRequest {
    private String prLink;
    private Long notebookId;
    private Long afterId;
    private Long beforeId;
}
