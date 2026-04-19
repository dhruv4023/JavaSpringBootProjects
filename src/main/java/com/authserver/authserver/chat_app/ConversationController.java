package com.authserver.authserver.chat_app;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping("/private")
    public ConversationResponse createOrGetPrivateConversation(
            Principal principal,
            @RequestBody PrivateChatRequest request
    ) {

        String currentUser = principal.getName();
        String otherUser = request.getUsername();

        String conversationId =
                conversationService.getOrCreatePrivateConversation(currentUser, otherUser);

        return new ConversationResponse(conversationId);
    }
}
