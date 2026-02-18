package com.authserver.authserver.user.models;

import java.util.List;

import com.authserver.authserver.base.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class AccessRights extends BaseModel {


    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private String route;

    @ManyToMany(mappedBy = "accessRights")
    @JsonIgnore
    private List<RoleModel> roles;
}
