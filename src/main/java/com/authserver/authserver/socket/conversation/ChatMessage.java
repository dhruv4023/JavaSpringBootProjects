package com.authserver.authserver.socket.conversation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String conversationId;
    private String from;
    private String content;
    private long timestamp;
}
