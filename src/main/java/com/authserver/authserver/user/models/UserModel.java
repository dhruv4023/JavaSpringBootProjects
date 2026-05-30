package com.authserver.authserver.user.models;

import java.util.UUID;

import com.authserver.authserver.base.BaseModel;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class UserModel extends BaseModel {
    public UserModel(UUID uuid) {
        super(uuid);
    }

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_uuid", nullable = false)
    private RoleModel role;

}
