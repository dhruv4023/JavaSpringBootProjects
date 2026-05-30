package com.authserver.authserver.communication.entry;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateEntry {
    private UUID uuid;
    private String name;
    private String title;
    private String content;
    private UUID userUuid;
}
