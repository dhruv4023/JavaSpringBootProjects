package com.authserver.authserver.socket.conversation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractChatSocketController {
    protected abstract void sendToConversation(String conversationId, Object payload);
    protected void routeMessage(ChatMessage msg) {
        if (msg.getConversationId() == null || msg.getConversationId().isBlank()) {
            throw new IllegalArgumentException("conversationId is required");
        }

        sendToConversation(msg.getConversationId(), msg);
    }
}
