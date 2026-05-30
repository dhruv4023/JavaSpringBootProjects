package com.authserver.authserver.user.entry;

import java.util.UUID;

import com.authserver.authserver.user.models.RoleModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntry {
    private UUID uuid;
    private String roleName;
    private String description;

    public RoleModel toModel() {
        RoleModel model = new RoleModel();
        model.setUuid(this.uuid);
        model.setRoleName(this.roleName);
        model.setDescription(this.description);
        return model;
    }
}
