package com.authserver.authserver.chat_app;

public class ConversationResponse {
    private String conversationId;

    public ConversationResponse(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationId() {
        return conversationId;
    }
}
