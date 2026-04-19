package com.authserver.authserver.chat_app;

import org.springframework.stereotype.Service;

import com.authserver.authserver.redis.RedisCacheService;

import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.*;

@Service
@AllArgsConstructor
public class ConversationService {

    private final RedisCacheService redisCacheService;

    public String getOrCreatePrivateConversation(String user1, String user2) {
        List<String> users = Arrays.asList(user1, user2);
        Collections.sort(users);

        String key = "chat:private:" + users.get(0) + ":" + users.get(1);
        String conversationId = redisCacheService.get(key, String.class);

        if (Objects.isNull(conversationId)) {
            conversationId = UUID.randomUUID().toString();
            redisCacheService.set(key, conversationId, Duration.ofHours(1));
        }

        return conversationId;
    }
}
