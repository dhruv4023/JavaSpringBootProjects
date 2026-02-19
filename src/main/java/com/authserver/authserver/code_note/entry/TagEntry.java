package com.authserver.authserver.code_note.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor
public class TagEntry {
    private Long id;
    private String name;    
}
