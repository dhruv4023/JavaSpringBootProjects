package com.authserver.authserver.event_queue;

import com.authserver.authserver.redis.RedisCacheService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.authserver.authserver.event_queue.models.EventQueue;
import com.authserver.authserver.event_queue.models.FinalStageEvents;
import com.authserver.authserver.event_queue.repository.EventQueueRepository;
import com.authserver.authserver.event_queue.repository.FailedEventRepository;
import com.authserver.authserver.user.manager.UserManager;
import com.authserver.authserver.user.models.UserModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public abstract class QueueHandler implements QueueHandlerInterface {
    private static final Random RANDOM = new Random();
    private final AtomicBoolean LOCK = new AtomicBoolean(false);
    private final RedisCacheService redisCacheService;
    private final EventQueueRepository queueRepo;
    private final UserManager userManager;
    private final FailedEventRepository failedRepo;

    protected abstract void send(EventQueueEntry events) throws Exception;

    public EventQueueRepository getQueueRepo() {
        return queueRepo;
    }

    @Async
    @Override
    public void handle(Integer maxAtATime) {
        log.info("Handler called for event type: {}", eventType());

        try {
            if (redisCacheService.getOrDefault(eventType(), Integer.class, 0) <= 0) {
                log.info("no data available for: {}, skipping proccess events", eventType());
                return;
            }
        } catch (Exception e) {
            if (queueRepo.countByEventType(eventType()) == 0) {
                log.info("db check; no data available for: {}, skipping proccess events", eventType());
                return;
            }
        }

        if (!LOCK.compareAndSet(false, true)) {
            log.info("handler already running skipping start again...");
            return;
        }

        try {
            processEvents(maxAtATime);
        } finally {
            LOCK.set(false);
        }

        log.info("Handler finished for event type: {}", eventType());
    }

    protected void processEvents(Integer maxAtATime) {
        log.info("Processing events for event type: {}", eventType());
        while (true) {
            Page<EventQueue> page;
            if (limitOnePerSender()) {
                page = queueRepo.fetchBatchDistinctSender(
                        eventType(),
                        PageRequest.of(0, maxAtATime));
            } else {
                page = queueRepo.findByEventTypeAndNextRetryAfterBefore(
                        eventType(),
                        Instant.now(),
                        PageRequest.of(0, maxAtATime));
            }

            List<EventQueue> events = page.getContent();
            if (events.isEmpty()) {
                break;
            }

            for (EventQueue event : events) {
                processEvent(event);
            }
        }
        log.info("Finished processing events for event type: {}", eventType());
    }

    @Transactional
    protected void processEvent(EventQueue event) {
        try {
            send(toEntry(event));
            moveToFinalStage(event, null);
            randomDelay();
        } catch (Exception e) {
            handleFailure(event, e.getMessage());
            sleep(30000);
        }
    }

    @Transactional
    private void handleFailure(EventQueue event, String error) {
        int retry = event.getRetryCount() == null ? 1 : event.getRetryCount();

        if (retry < maxRetryCount() && shouldRetry(error)) {
            event.setRetryCount(retry + 1);
            event.setNextRetryAfter(Instant.now().plusMillis(nextRetryAfterXMinutes() * 60 * 1000));
            queueRepo.save(event);
        } else {
            moveToFinalStage(event, error);
        }
    }

    @Transactional
    private void moveToFinalStage(EventQueue event, String error) {
        if (Objects.isNull(error)) {
            event.setPayload(null);
            event.setStatus(QueueStatus.DONE);
        } else {
            event.setStatus(QueueStatus.FAILED);
        }

        FinalStageEvents failedEvent = FinalStageEvents.builder()
                .eventType(event.getEventType())
                .status(event.getStatus())
                .payload(event.getPayload())
                .retryCount(event.getRetryCount())
                .sender(event.getSender())
                .error(error)
                .build();

        failedRepo.save(failedEvent);
        queueRepo.delete(event);
        try {
            redisCacheService.decrement(eventType());
        } catch (Exception e) {
            log.info("Failed to decrease count of {}", eventType());
        }
    }

    protected boolean shouldRetry(String error) {
        return true;
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    private void randomDelay() {
        try {

            int delay = 3000 + RANDOM.nextInt(7000);
            Thread.sleep(delay);

        } catch (InterruptedException ignored) {
        }
    }

    private EventQueueEntry toEntry(EventQueue entity) {
        return EventQueueEntry.builder()
                .id(entity.getId())
                .eventType(entity.getEventType())
                .status(entity.getStatus())
                .payload(entity.getPayload())
                .retryCount(entity.getRetryCount())
                .build();
    }

    @Override
    public boolean addToQueue(EventQueueEntry entry) {
        UserModel user = Objects.nonNull(entry.getSenderId()) ? userManager.findUserModelByID(entry.getSenderId())
                : null;

        EventQueue event = EventQueue.builder()
                .eventType(eventType())
                .nextRetryAfter(Instant.now())
                .status(QueueStatus.PENDING)
                .payload(entry.getPayload())
                .retryCount(entry.getRetryCount())
                .sender(user)
                .build();
        queueRepo.save(event);
        try {
            redisCacheService.increment(eventType());
        } catch (Exception e) {
            log.info("Failed to increase count of {}", eventType());
        }
        return true;
    }

    @Override
    public Integer handlMaxAtaTime() {
        return 10;
    }

    @Override
    public boolean limitOnePerSender() {
        return true;
    }

    @Override
    public int maxRetryCount() {
        return 3;
    }

    @Override
    public Integer nextRetryAfterXMinutes() {
        return 2;
    }
}
