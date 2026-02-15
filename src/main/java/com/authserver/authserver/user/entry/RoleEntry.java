package com.authserver.authserver.user.entry;

import com.authserver.authserver.user.models.RoleModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntry {
    private Long id;
    private String roleName;
    private String description;

    public static RoleEntry fromModel(RoleModel model) {
        if (model == null) return null;
        return new RoleEntry(model.getId(),model.getRoleName(), model.getDescription());
    }

    public RoleModel toModel() {
        RoleModel model = new RoleModel();
        model.setId(this.id);
        model.setRoleName(this.roleName);
        model.setDescription(this.description);
        return model;
    }
}
