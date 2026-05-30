package com.authserver.authserver.event_queue.entry;

import java.util.UUID;

import com.authserver.authserver.event_queue.QueueStatus;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class EventQueueEntry {

    private final UUID uuid;
    private final String eventType;
    private final QueueStatus status;
    private final Integer retryCount;

    @Setter
    private String error;

    @Setter
    private UUID senderUuid;

    @Setter
    private String payload;

    public EventQueueEntry(UUID senderUuid, String payload) {
        this.uuid = null;
        this.eventType = null;
        this.status = null;
        this.retryCount = null;
        this.senderUuid = senderUuid;
        this.payload = payload;
    }
}
