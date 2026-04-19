package com.authserver.authserver.socket;

import lombok.extern.slf4j.Slf4j;

import java.security.Principal;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.*;

@Slf4j
public abstract class AbstractWebSocketEventListener {

    @EventListener
    public final void handleConnect(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = accessor.getSessionId();
        String username = accessor.getUser() != null
                ? accessor.getUser().getName()
                : "anonymous";

        log.info("✅ CONNECTED: sessionId={}, username={}", sessionId, username);

        onConnect(sessionId, username);
    }

    @EventListener
    public final void handleDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();

        Principal user = event.getUser();
        String username = user != null ? user.getName() : "anonymous";

        log.info("❌ DISCONNECTED: sessionId={}, username={}", sessionId, username);

        onDisconnect(sessionId, username);
    }

    // 🔥 Abstract hooks (you override these)
    protected abstract void onConnect(String sessionId, String username);

    protected abstract void onDisconnect(String sessionId, String username);
}
