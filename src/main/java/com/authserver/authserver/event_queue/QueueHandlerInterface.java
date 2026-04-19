package com.authserver.authserver.event_queue;

import com.authserver.authserver.event_queue.entry.EventQueueEntry;
import com.authserver.authserver.event_queue.repository.EventQueueRepository;

public interface QueueHandlerInterface {
    void handle();

    boolean addToQueue(EventQueueEntry entry);

    public abstract String getEventType();

    public abstract EventQueueRepository getQueueRepo();
}
