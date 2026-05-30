package com.authserver.authserver.communication.event;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumerUserCreatedEvent {
    private UUID senderUuid;
    private String toEmail;
    private String subject;
    private String body;
    private byte[] attachmentBytes;
    private String attachmentFileName;
}
