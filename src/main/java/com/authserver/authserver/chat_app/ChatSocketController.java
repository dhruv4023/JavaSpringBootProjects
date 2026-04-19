package com.authserver.authserver.chat_app;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.authserver.authserver.socket.conversation.AbstractChatSocketController;
import com.authserver.authserver.socket.conversation.ChatMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ChatSocketController extends AbstractChatSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void handleMessage(Principal principal, ChatMessage message) {

        message.setFrom(principal.getName());
        message.setTimestamp(System.currentTimeMillis());

        routeMessage(message);
    }

    @Override
    protected void sendToConversation(String conversationId, Object payload) {
        messagingTemplate.convertAndSend(
                "/topic/conversation." + conversationId,
                payload
        );
    }
}