package com.pozdeev.HelloWorld.security;

import com.pozdeev.HelloWorld.models.security.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

public class JwtUserAuthenticationToken implements Authentication {

    private final String username;
    private final String role;
    private final Set<SimpleGrantedAuthority> authorities;
    private boolean isAuthenticated;

    public JwtUserAuthenticationToken(String username, String role, Set<SimpleGrantedAuthority> authorities) {
        this.username = username;
        this.role = role;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getPrincipal() {
        return username;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return role;
    }

}
