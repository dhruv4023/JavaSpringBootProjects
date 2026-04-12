package com.authserver.authserver.event_queue;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueScheduler {

    private final List<QueueHandler> handlers;

    @Scheduled(fixedDelay = 10 * 1000) // millis
    public void processQueues() {

        log.info("Scheduler triggered...");

        for (QueueHandlerInterface handler : handlers) {
            try {
                handler.handle(10);
            } catch (Exception e) {
                log.error("Error in handler: {}", handler.eventType(), e);
            }
        }
    }
}