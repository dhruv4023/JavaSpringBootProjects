package com.authserver.authserver.user.services;
import org.springframework.stereotype.Service;
import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.base.service.BaseService;
import com.authserver.authserver.user.entry.UserEntry;

@Service
public class UserService extends BaseService<UserEntry, Long>{

    public UserService(BaseManager<?, Long, UserEntry> manager) {
        super(manager);
    }

    // public ResponseObject updateUser(String username, UserEntry updatedEntry) {
    //     try {
    //         Optional<UserModel> optionalUser = userRepository.findByUsername(username);

    //         if (optionalUser.isEmpty()) {
    //             throw new RuntimeException("User not found with ID: " + username);
    //         }

    //         UserModel existingUser = optionalUser.get();

    //         if (Objects.nonNull(updatedEntry.getUsername())) {
    //             existingUser.setUsername(updatedEntry.getUsername());
    //         }
    //         if (Objects.nonNull(updatedEntry.getEmail())) {
    //             existingUser.setEmail(updatedEntry.getEmail());
    //         }
    //         if (Objects.nonNull(updatedEntry.getRoleEntry())) {
    //             RoleModel role = roleService.getRoleByName(updatedEntry.getRoleEntry().getRoleName());
    //             existingUser.setRole(role);
    //         }

    //         UserModel savedUser = userRepository.save(existingUser);

    //         return new ResponseObject(true, "Login successful", UserEntry.fromModel(savedUser));

    //     } catch (Exception e) {
    //         return new ResponseObject(false, e.getMessage(), null);
    //     }
    // }

}
