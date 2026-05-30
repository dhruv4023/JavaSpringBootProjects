package com.authserver.authserver.user.entry;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntry {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    private String username;

    @Email
    @NotBlank
    private String email;

    private RoleEntry roleEntry;
}
