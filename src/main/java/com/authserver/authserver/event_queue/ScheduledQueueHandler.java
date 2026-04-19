package com.authserver.authserver.event_queue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.authserver.authserver.event_queue.repository.EventQueueRepository;
import com.authserver.authserver.event_queue.repository.FailedEventRepository;
import com.authserver.authserver.redis.RedisCacheService;
import com.authserver.authserver.user.manager.UserManager;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public abstract class ScheduledQueueHandler extends QueueHandler {

    public ScheduledQueueHandler(RedisCacheService redisCacheService, EventQueueRepository queueRepo,
            UserManager userManager, FailedEventRepository failedRepo, String eventType) {
        super(redisCacheService, queueRepo, userManager, failedRepo, eventType);
    }

    @Scheduled(fixedDelay = 10 * 1000) // millis
    public void processQueues() {
        log.info("Scheduler triggered...");
        try {
            handle();
        } catch (Exception e) {
            log.error("Error in handler: {}", getEventType(), e);
        }
    }
}
