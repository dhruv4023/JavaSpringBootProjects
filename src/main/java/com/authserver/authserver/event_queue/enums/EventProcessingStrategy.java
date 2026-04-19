package com.authserver.authserver.event_queue.enums;

public enum EventProcessingStrategy {
    ONE_PER_SENDER,
    BY_EVENT_TYPE,
    BY_USER,
}
