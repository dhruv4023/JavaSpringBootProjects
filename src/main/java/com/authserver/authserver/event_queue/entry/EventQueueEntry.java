package com.authserver.authserver.event_queue.entry;

import com.authserver.authserver.event_queue.QueueStatus;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class EventQueueEntry {

    private final Long id;
    private final String eventType;
    private final QueueStatus status;
    private final Integer retryCount;

    @Setter
    private String error;

    @Setter
    private Long senderId;

    @Setter
    private String payload;

    public EventQueueEntry(Long senderId, String payload) {
        this.id = null;
        this.eventType = null;
        this.status = null;
        this.retryCount = null;
        this.senderId = senderId;
        this.payload = payload;
    }
}
