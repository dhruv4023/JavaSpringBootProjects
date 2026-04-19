package com.authserver.authserver.socket.notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry userRegistry;

    public boolean isUserOnline(String username) {
        return userRegistry.getUser(username) != null;
    }

    public void sendNotification(String username, String payload) {
        if (!isUserOnline(username)) {
            throw new RuntimeException("User is not online");
        }
        messagingTemplate.convertAndSendToUser(username, "/topic/notifications", payload);
    }

    public void sendNotification(String payload) {
        messagingTemplate.convertAndSend("/topic/notifications", payload);
    }
}
