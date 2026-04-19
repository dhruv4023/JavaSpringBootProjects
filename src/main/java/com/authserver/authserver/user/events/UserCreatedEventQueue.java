package com.authserver.authserver.user.events;

import org.springframework.stereotype.Component;

import com.authserver.authserver.event_queue.ScheduledQueueHandler;
import com.authserver.authserver.event_queue.entry.EventQueueEntry;
import com.authserver.authserver.event_queue.repository.EventQueueRepository;
import com.authserver.authserver.event_queue.repository.FailedEventRepository;
import com.authserver.authserver.redis.RedisCacheService;
import com.authserver.authserver.user.manager.UserManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserCreatedEventQueue extends ScheduledQueueHandler {
    private final UserEventPublisher userEventPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();
   
   public UserCreatedEventQueue(RedisCacheService redisCacheService, EventQueueRepository queueRepo, UserManager userManager,
            FailedEventRepository failedRepo, UserEventPublisher userEventPublisher) {
        super(redisCacheService, queueRepo, userManager, failedRepo, "user.created");
        this.userEventPublisher = userEventPublisher;
    }

    @Override
    protected void send(EventQueueEntry events) throws Exception{
        ProducerEmailEvent payload;
        payload = objectMapper.readValue(events.getPayload(), ProducerEmailEvent.class);
        userEventPublisher.publishUserCreated(payload);
    } 
}
