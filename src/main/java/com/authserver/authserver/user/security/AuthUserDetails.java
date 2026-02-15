package com.authserver.authserver.user.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.authserver.authserver.user.models.UserModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthUserDetails implements UserDetails {

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public AuthUserDetails(UserModel user) {

        this.username = user.getUsername();
        this.password = user.getPassword();

        List<GrantedAuthority> authRouteList = new ArrayList<>();

        authRouteList.add(new SimpleGrantedAuthority(
                "ROLE_" + user.getRole().getRoleName()));

        user.getRole().getAccessRights().forEach(right -> authRouteList.add(
                new SimpleGrantedAuthority(right.getRoute())));

        this.authorities = authRouteList;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
