package com.authserver.authserver.event_queue.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.authserver.authserver.base.BaseModel;
import com.authserver.authserver.event_queue.QueueStatus;
import com.authserver.authserver.user.models.UserModel;

@Entity
@Table(name = "failed_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinalStageEvents extends BaseModel {

    @Id
    @UuidGenerator
    @Column(name = "uuid", columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "payload", columnDefinition = "json")
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QueueStatus status;

    @ManyToOne
    @JoinColumn(name = "sender_uuid")
    private UserModel sender;

    @Column(name = "error")
    private String error;

    @Column(name = "retry_count")
    private Integer retryCount;

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