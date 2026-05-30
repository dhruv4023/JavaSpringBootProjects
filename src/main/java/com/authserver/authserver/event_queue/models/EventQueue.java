package com.authserver.authserver.event_queue.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.authserver.authserver.base.BaseModel;
import com.authserver.authserver.event_queue.QueueStatus;
import com.authserver.authserver.user.models.UserModel;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "event_queue", indexes = {
        @Index(name = "idx_event_queue_sender", columnList = "sender_uuid"),
        @Index(name = "idx_event_queue_event_type", columnList = "event_type"),
})
public class EventQueue extends BaseModel {
    @Id
    @UuidGenerator
    @Column(name = "uuid", columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "payload", columnDefinition = "json", nullable = false)
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QueueStatus status;

    @ManyToOne
    @JoinColumn(name = "sender_uuid")
    private UserModel sender;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "next_retry_after")
    private Instant nextRetryAfter;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = QueueStatus.PENDING;
        }
        if (retryCount == null) {
            retryCount = 0;
        }
    }
}