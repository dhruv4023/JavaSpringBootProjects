package com.authserver.authserver.communication.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateEntry {
    private Long id;
    private String name;
    private String title;
    private String content;
    private Long userId;
}
