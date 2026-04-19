package com.authserver.authserver.event_queue.entry;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class BulkResult {
    private Long eventId;
    private boolean success;
    private String error;
}
