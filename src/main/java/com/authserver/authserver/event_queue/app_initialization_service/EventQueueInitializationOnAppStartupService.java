
package com.authserver.authserver.event_queue.app_initialization_service;

import com.authserver.authserver.base.app_initialization_service.AbstractStartupInitializer;
import com.authserver.authserver.event_queue.repository.EventQueueRepository;
import com.authserver.authserver.redis.RedisCacheService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventQueueInitializationOnAppStartupService extends AbstractStartupInitializer {

    private final EventQueueRepository queueRepo;
    private final RedisCacheService redisCacheService;

    @Override
    protected void initialize() {
        List<Object[]> counts = queueRepo.countPendingGroupedByEventType();
        
        for (Object[] row : counts) {
            String eventType = (String) row[0];
            Long count = (Long) row[1];
            redisCacheService.set(eventType, count.intValue());
        }
    }
}