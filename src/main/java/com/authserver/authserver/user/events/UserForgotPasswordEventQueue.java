package com.authserver.authserver.user.events;

import org.springframework.stereotype.Component;

import com.authserver.authserver.event_queue.EventQueueEntry;
import com.authserver.authserver.event_queue.QueueHandler;
import com.authserver.authserver.event_queue.repository.EventQueueRepository;
import com.authserver.authserver.event_queue.repository.FailedEventRepository;
import com.authserver.authserver.redis.RedisCacheService;
import com.authserver.authserver.user.manager.UserManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserForgotPasswordEventQueue extends QueueHandler {
    private final UserEventPublisher userEventPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserForgotPasswordEventQueue(RedisCacheService redisCacheService, EventQueueRepository queueRepo,
            UserManager userManager,
            FailedEventRepository failedRepo, UserEventPublisher userEventPublisher) {
        super(redisCacheService, queueRepo, userManager, failedRepo);
        this.userEventPublisher = userEventPublisher;
    }

    @Override
    public String eventType() {
        return "user.forgot.password";
    }

    @Override
    public boolean limitOnePerSender() {
        return true;
    }

    @Override
    protected void send(EventQueueEntry events) throws Exception {
        ProducerEmailEvent payload = objectMapper.readValue(events.getPayload(), ProducerEmailEvent.class);
        userEventPublisher.publishForgotPassword(payload);
    }
}
