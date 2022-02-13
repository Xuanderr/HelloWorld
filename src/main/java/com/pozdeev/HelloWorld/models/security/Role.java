package com.pozdeev.HelloWorld.models.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(Permission.USERS_UPDATE, Permission.USERS_DELETE, Permission.COMMENTS_UPDATE, Permission.TOKEN_REFRESH)),
    ADMIN(Set.of(Permission.USERS_READ, Permission.USERS_PROPERTIES_UPDATE, Permission.USERS_DELETE, Permission.TAGS_CREATE,
            Permission.TAGS_UPDATE, Permission.TAGS_DELETE, Permission.TOKEN_REFRESH));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return this.name() + "{ " + "permissions = " + permissions + " }";
    }
}
