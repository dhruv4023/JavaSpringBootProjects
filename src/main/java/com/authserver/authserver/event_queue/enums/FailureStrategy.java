package com.authserver.authserver.event_queue.enums;

public enum FailureStrategy {
    IGNORE, // no retry, no DB (fire-and-forget)
    RETRY, // retry only, no final storage
    RETRY_AND_STORE, // retry + store in DB if exhausted
    RETRY_STORE_ALERT // retry + store + alert/log critical
}
