package com.authserver.authserver.event_queue;

public interface QueueHandlerInterface {

    String eventType();

    void handle(Integer maxAtATime);

    boolean limitOnePerSender();

    boolean addToQueue(EventQueueEntry entry);
}
