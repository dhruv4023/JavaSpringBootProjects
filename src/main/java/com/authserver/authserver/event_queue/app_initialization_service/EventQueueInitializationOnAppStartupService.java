
package com.authserver.authserver.event_queue.app_initialization_service;

import com.authserver.authserver.base.app_initialization_service.AbstractStartupInitializer;
import com.authserver.authserver.event_queue.QueueHandlerInterface;
import com.authserver.authserver.event_queue.repository.EventQueueRepository;
import com.authserver.authserver.redis.RedisCacheService;

import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventQueueInitializationOnAppStartupService extends AbstractStartupInitializer {

    private final EventQueueRepository queueRepo;
    private final RedisCacheService redisCacheService;
    private final List<QueueHandlerInterface> queueHandlers;

    @Override
    protected void initialize() {
        List<Object[]> counts = queueRepo.countPendingGroupedByEventType();
        Set<String> events = new HashSet<>();
        for (QueueHandlerInterface handler : queueHandlers) {
            events.add(handler.getEventType());
        }

        for (Object[] row : counts) {
            String eventType = (String) row[0];
            Long count = (Long) row[1];
            redisCacheService.set(eventType, count.intValue());
            events.remove(eventType);
        }
        for (String event : events) {
            redisCacheService.set(event, 0);
        }
    }
}