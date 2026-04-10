package com.authserver.authserver.user.mapper;

import java.util.Objects;
import org.springframework.stereotype.Component;

import com.authserver.authserver.base.ConvertorInterface;
import com.authserver.authserver.user.entry.UserEntry;
import com.authserver.authserver.user.models.UserModel;

@Component
public class UserConvertor implements ConvertorInterface<UserEntry, UserModel> {

    private final RoleConvertor roleConvertor;

    UserConvertor(RoleConvertor roleConvertor) {
        this.roleConvertor = roleConvertor;
    }

    @Override
    public UserModel toModel(UserEntry entry, UserModel existing) {
        UserModel user = existing != null ? existing : new UserModel();
        if (Objects.nonNull(entry.getUsername())) {
            user.setUsername(entry.getUsername());
        }
        if (Objects.nonNull(entry.getEmail())) {
            user.setEmail(entry.getEmail());
        }
        return user;
    }

    @Override
    public UserEntry toEntry(UserModel model) {
        UserEntry entry = new UserEntry();
        entry.setId(model.getId());
        entry.setUsername(model.getUsername());
        entry.setEmail(model.getEmail());
        entry.setRoleEntry(roleConvertor.toEntry(model.getRole()));
        return entry;
    }

}
