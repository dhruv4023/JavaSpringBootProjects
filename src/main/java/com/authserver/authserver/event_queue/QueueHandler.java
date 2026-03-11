package com.authserver.authserver.event_queue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.authserver.authserver.event_queue.models.EventQueue;
import com.authserver.authserver.event_queue.models.FailedEvents;
import com.authserver.authserver.event_queue.repository.EventQueueRepository;
import com.authserver.authserver.event_queue.repository.FailedEventRepository;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public abstract class QueueHandler implements QueueHandlerInterface {

    private static final int MAX_RETRY = 3;
    private static final Random RANDOM = new Random();

    private final EventQueueRepository queueRepo;
    private final FailedEventRepository failedRepo;

    public QueueHandler(EventQueueRepository queueRepo,
            FailedEventRepository failedRepo) {
        this.queueRepo = queueRepo;
        this.failedRepo = failedRepo;
    }

    protected abstract void send(EventQueueEntry events) throws Exception;

    protected abstract List<EventQueueEntry> send(List<EventQueueEntry> events) throws Exception;

    @Override
    public void handle(Integer maxAtATime) {

        while (true) {

            Page<EventQueue> page = queueRepo.fetchBatchDistinctSender(
                    eventType(),
                    PageRequest.of(0, maxAtATime));

            List<EventQueue> events = page.getContent();

            if (events.isEmpty()) {
                break;
            }

            try {

                List<EventQueueEntry> failed = send(
                        events.stream().map(this::toEntry).toList());

                Set<Long> failedIds = failed.stream()
                        .map(EventQueueEntry::getId)
                        .collect(Collectors.toSet());

                for (EventQueue event : events) {

                    if (failedIds.contains(event.getId())) {

                        EventQueueEntry failedEntry = failed.stream()
                                .filter(e -> e.getId().equals(event.getId()))
                                .findFirst()
                                .orElse(null);

                        handleFailure(event,
                                failedEntry != null ? failedEntry.getError() : "Unknown error");

                    } else {

                        queueRepo.delete(event);
                    }
                }

                randomDelay();

            } catch (Exception e) {

                for (EventQueue event : events) {
                    handleFailure(event, e.getMessage());
                }

                sleep(30000);
            }
        }
    }

    private void handleFailure(EventQueue event, String error) {

        int retry = event.getRetryCount() == null ? 0 : event.getRetryCount();

        if (retry < MAX_RETRY) {

            event.setRetryCount(retry + 1);
            event.setStatus(QueueStatus.PENDING);
            queueRepo.save(event);

        } else {

            moveToFailed(event, error);
        }
    }

    private void moveToFailed(EventQueue event, String error) {

        FailedEvents failedEvent = FailedEvents.builder()
                .eventType(event.getEventType())
                .status(QueueStatus.FAILED)
                .payload(event.getPayload())
                .retryCount(event.getRetryCount())
                .sender(event.getSender())
                .error(error)
                .build();

        Objects.requireNonNull(failedEvent);
        failedRepo.save(failedEvent);

        queueRepo.delete(event);
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
}