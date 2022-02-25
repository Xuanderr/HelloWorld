package com.pozdeev.HelloWorld.models.entities.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pozdeev.HelloWorld.models.security.Role;
import com.pozdeev.HelloWorld.models.security.Status;

import java.time.LocalDateTime;

public class SmallUser extends User{

    private String name;

    public SmallUser(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Override
    public Long getUserId() {
        return null;
    }

    @Override
    public void setUserId(Long userId) { }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public void setEmail(String login) { }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void setPassword(String password) { }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Override
    public Role getRole() {
        return null;
    }

    @Override
    public void setRole(Role role) { }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Override
    public Status getStatus() {
        return null;
    }

    @Override
    public void setStatus(Status status) { }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Override
    public LocalDateTime getCreated() {
        return null;
    }

    @Override
    public void setCreated(LocalDateTime created) { }

}
