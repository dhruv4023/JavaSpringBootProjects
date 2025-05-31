package com.authserver.authserver.user.entry;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginEntry {
    private String username;
    private String password;
}
