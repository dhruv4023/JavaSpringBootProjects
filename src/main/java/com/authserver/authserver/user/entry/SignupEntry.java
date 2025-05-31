package com.authserver.authserver.user.entry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupEntry {

    private String username;
    private String password;
    private String email;
    private String roleName;
}
