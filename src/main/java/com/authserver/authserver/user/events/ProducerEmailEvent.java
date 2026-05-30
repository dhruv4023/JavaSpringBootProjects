package com.authserver.authserver.user.events;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProducerEmailEvent {
    // FIXME: remove this field or remove senderUuid from EventQueue table
    private UUID senderUuid;
    private String toEmail;
    private String subject;
    private String body;
    private byte[] attachmentBytes;
    private String attachmentFileName;
}
