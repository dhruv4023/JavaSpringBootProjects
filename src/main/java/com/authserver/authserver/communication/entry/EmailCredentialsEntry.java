package com.authserver.authserver.communication.entry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailCredentialsEntry {
    private String passcode;
    private Long userId;
}
