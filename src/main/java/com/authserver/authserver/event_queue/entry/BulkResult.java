package com.authserver.authserver.event_queue.entry;

import java.util.UUID;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class BulkResult {
    private UUID eventUuid;
    private boolean success;
    private String error;
}
