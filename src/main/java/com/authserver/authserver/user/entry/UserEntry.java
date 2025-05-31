package com.authserver.authserver.user.entry;


import java.time.Instant;

import com.authserver.authserver.user.models.UserModel;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntry {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String username;

    private String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private RoleEntry roleEntry;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;

    public static UserEntry fromModel(UserModel userModel) {
        if (userModel == null || userModel.getRole() == null) {
            return null;
        }
        return new UserEntry(
                userModel.getId(),
                userModel.getUsername(),
                userModel.getEmail(),
                RoleEntry.fromModel(userModel.getRole()),
                userModel.getCreatedAt(),
                userModel.getUpdatedAt());
    }
}
