package com.authserver.authserver.event_queue;

import com.authserver.authserver.redis.RedisCacheService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.authserver.authserver.event_queue.entry.BulkResult;
import com.authserver.authserver.event_queue.entry.EventQueueEntry;
import com.authserver.authserver.event_queue.enums.EventProcessingStrategy;
import com.authserver.authserver.event_queue.enums.EventSendType;
import com.authserver.authserver.event_queue.enums.FailureStrategy;
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
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
    private final String eventType;

    protected int maxRetryCount = 3;
    protected int handlMaxAtaTime = 10;
    protected int nextRetryAfterXMinutes = 1;
    protected int maxRandomDelayInMinutes = 7;
    protected EventSendType eventSendType = EventSendType.ONE_BY_ONE;
    protected FailureStrategy failureStrategy = FailureStrategy.RETRY_AND_STORE;
    protected EventProcessingStrategy sendType = EventProcessingStrategy.ONE_PER_SENDER;

    @Override
    public EventQueueRepository getQueueRepo() {
        return queueRepo;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Async
    @Override
    public void handle() {
        log.info("Handler called for event type: {}", eventType);

        try {
            if (redisCacheService.getOrDefault(eventType, Integer.class, 0) <= 0) {
                log.info("no data available for: {}, skipping proccess events", eventType);
                return;
            }
        } catch (Exception e) {
            if (queueRepo.countByEventType(eventType) == 0) {
                log.info("db check; no data available for: {}, skipping proccess events", eventType);
                return;
            }
        }

        if (!LOCK.compareAndSet(false, true)) {
            log.info("handler already running skipping start again...");
            return;
        }

        try {
            processEvents();
        } finally {
            LOCK.set(false);
        }

        log.info("Handler finished for event type: {}", eventType);
    }

    @Override
    public boolean addToQueue(EventQueueEntry entry) {
        UserModel user = Objects.nonNull(entry.getSenderId())
                ? userManager.findUserModelByID(entry.getSenderId())
                : null;

        EventQueue event = EventQueue.builder()
                .eventType(eventType)
                .nextRetryAfter(Instant.now())
                .status(QueueStatus.PENDING)
                .payload(entry.getPayload())
                .retryCount(entry.getRetryCount())
                .sender(user)
                .build();

        queueRepo.save(event);
        try {
            redisCacheService.increment(eventType);
        } catch (Exception e) {
            log.info("Failed to increase count of {}", eventType);
        }
        return true;
    }

    protected void send(EventQueueEntry event) throws Exception {
        throw new UnsupportedOperationException("Not implemented for eventType " + eventType);
    }

    protected List<BulkResult> sendBulk(List<EventQueueEntry> events) {
        throw new UnsupportedOperationException("Not implemented for eventType " + eventType);
    }

    protected void processEvents() {
        log.info("Processing events for event type: {}", eventType);
        while (true) {
            List<EventQueue> events = getData().getContent();
            if (events.isEmpty()) {
                break;
            }
            sendAllEvents(events);
        }
        log.info("Finished processing events for event type: {}", eventType);
    }

    private void sendAllEvents(List<EventQueue> events) {
        switch (eventSendType) {
            case ONE_BY_ONE:
                for (EventQueue event : events) {
                    processEventOneByOne(event);
                }
                break;

            case BULK:
                handleBulk(events);
                break;

            default:
                throw new UnsupportedOperationException("Not implemented");
        }
    }

    @Transactional
    protected void processEventOneByOne(EventQueue event) {
        try {
            send(toEntry(event));
            handleAfterSendOrFailure(event, null);
            randomDelay();
        } catch (Exception e) {
            handleAfterSendOrFailure(event, e.getMessage());
            sleep(30000);
        }
    }

    protected void handleBulk(List<EventQueue> events) {

        List<EventQueueEntry> entries = events.stream()
                .map(this::toEntry)
                .toList();

        List<BulkResult> results = sendBulk(entries);

        Map<Long, EventQueue> eventMap = events.stream()
                .collect(Collectors.toMap(EventQueue::getId, e -> e));

        for (BulkResult result : results) {
            EventQueue event = eventMap.get(result.getEventId());
            if (Objects.nonNull(event))
                handleAfterSendOrFailure(event, result.getError());
        }
    }

    private void handleAfterSendOrFailure(EventQueue event, String error) {
        switch (failureStrategy) {
            case IGNORE:
                deleteEvent(event);
                break;
            case RETRY:
                if (Objects.nonNull(error)) {
                    boolean isHandled = handleFailure(event, error);
                    if (!isHandled) {
                        deleteEvent(event);
                    }
                }
                break;
            case RETRY_AND_STORE:
                if (Objects.nonNull(error)) {
                    boolean isHandled = handleFailure(event, error);
                    if (!isHandled) {
                        moveToFinalStage(event, error);
                    }
                } else {
                    moveToFinalStage(event, error);
                }
                break;
            case RETRY_STORE_ALERT:
                throw new UnsupportedOperationException("not implemented");
            default:
                throw new UnsupportedOperationException("Unsupported failure strategy");
        }
    }

    private void deleteEvent(EventQueue event) {
        queueRepo.delete(event);
    }

    protected boolean shouldRetry(String error) {
        return true;
    }

    private Page<EventQueue> getData() {
        switch (sendType) {
            case ONE_PER_SENDER:
                return queueRepo.fetchBatchDistinctSender(
                        eventType,
                        PageRequest.of(0, handlMaxAtaTime));

            case BY_EVENT_TYPE:
                return queueRepo.findByEventTypeAndNextRetryAfterBefore(
                        eventType,
                        Instant.now(),
                        PageRequest.of(0, handlMaxAtaTime));
            case BY_USER:
                throw new UnsupportedOperationException("Not implemented");
            default:
                throw new RuntimeException("Invalid send type");
        }
    }

    @Transactional
    private boolean handleFailure(EventQueue event, String error) {
        int retry = event.getRetryCount() == null ? 0 : event.getRetryCount();
        if (retry < maxRetryCount && shouldRetry(error)) {
            event.setRetryCount(retry + 1);
            event.setNextRetryAfter(Instant.now().plusMillis(nextRetryAfterXMinutes * 60 * 1000));
            queueRepo.save(event);
            return true;
        }
        return false;
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
        deleteEvent(event);
        try {
            redisCacheService.decrement(eventType);
        } catch (Exception e) {
            log.info("Failed to decrease count of {}", eventType);
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

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    private void randomDelay() {
        try {
            int delay = 3000 + RANDOM.nextInt(maxRandomDelayInMinutes * 1000);
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
        }
    }
}
