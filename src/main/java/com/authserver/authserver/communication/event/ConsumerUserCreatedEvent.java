package com.authserver.authserver.communication.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerUserCreatedEvent {
    private Long userId;
    private String toEmail;
    private String subject;
    private String body;
    private byte[] attachmentBytes;
    private String attachmentFileName;
}
