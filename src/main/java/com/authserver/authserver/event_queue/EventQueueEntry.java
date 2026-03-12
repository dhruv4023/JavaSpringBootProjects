package com.authserver.authserver.event_queue;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class EventQueueEntry {

    private final Long id;
    private final String eventType;
    private final QueueStatus status;

    @Setter
    private String error;

    @Setter
    private Long senderId;

    @Setter
    private String payload;

    @Setter
    private Integer retryCount;

    public EventQueueEntry(Long senderId, String payload, Integer retryCount) {
        this.id = null;
        this.eventType = null;
        this.status = null;
        this.senderId = senderId;
        this.payload = payload;
        this.retryCount = retryCount;
    }
}
