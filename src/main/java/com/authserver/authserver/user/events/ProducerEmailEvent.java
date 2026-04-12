package com.authserver.authserver.user.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProducerEmailEvent {
    private Long userId;
    private String toEmail;
    private String subject;
    private String body;
    private byte[] attachmentBytes;
    private String attachmentFileName;
    // getters/setters
}
