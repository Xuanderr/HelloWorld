package com.pozdeev.HelloWorld.models.security;


public enum Permission {
    USERS_READ("users:read"),
    USERS_UPDATE("users:update"),
    USERS_PROPERTIES_UPDATE("users:properties_update"),
    USERS_DELETE("users:delete"),
    COMMENTS_UPDATE("comment:update"),
    TAGS_CREATE("tags:create"),
    TAGS_UPDATE("tags:update"),
    TAGS_DELETE("tags:delete"),
    TOKEN_REFRESH("token:refresh");


    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        return this.name() + " { " + "permission = " + permission + " }";
    }
}
