package com.authserver.authserver.event_queue.models;

import jakarta.persistence.*;
import lombok.*;
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
public class FailedEvents extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "payload", columnDefinition = "json", nullable = false)
    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QueueStatus status;

    @Column(name = "sender")
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