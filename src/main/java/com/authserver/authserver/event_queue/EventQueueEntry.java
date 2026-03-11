package com.authserver.authserver.event_queue;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
class EventQueueEntry {

    private final Long id;
    private final String eventType;
    private final QueueStatus status;

    @Setter
    private String error;

    @Setter
    private String sender;

    @Setter
    private String payload;

    @Setter
    private Integer retryCount;
}
