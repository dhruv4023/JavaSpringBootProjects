package com.authserver.authserver.user.models;

import java.util.Set;

import com.authserver.authserver.base.BaseModel;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RoleModel extends BaseModel {

    public RoleModel(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roleName;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_access_rights", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "access_rights_id"))
    private Set<AccessRights> accessRights;
}
