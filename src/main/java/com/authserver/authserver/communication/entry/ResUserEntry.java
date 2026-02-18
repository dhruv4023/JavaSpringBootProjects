package com.authserver.authserver.communication.entry;

import com.authserver.authserver.user.entry.UserEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResUserEntry extends UserEntry {
    private EmailCredentialsEntry emailCredentialsEntry;
}