package com.authserver.authserver.user.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProducerEmailEvent {
    // FIXME: remove this field or remove senderId from EventQueue table
    private Long senderId;
    private String toEmail;
    private String subject;
    private String body;
    private byte[] attachmentBytes;
    private String attachmentFileName;
}
