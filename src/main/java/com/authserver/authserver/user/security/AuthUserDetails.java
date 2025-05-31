package com.authserver.authserver.user.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.authserver.authserver.user.models.UserModel;

import java.util.Collection;
import java.util.Collections;

public class AuthUserDetails implements UserDetails {

    private String name;
    private String password;
    private GrantedAuthority authority;

    public AuthUserDetails(UserModel userInfo) {
        name = userInfo.getUsername();
        password = userInfo.getPassword();
        authority = new SimpleGrantedAuthority(userInfo.getRole().getRoleName());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}