package com.authserver.authserver.chat_app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateChatRequest {
    private String username;

    // getter/setter
}
