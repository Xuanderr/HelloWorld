package com.pozdeev.HelloWorld.models.system_entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserProperties {

    @JsonIgnore
    private String email;
    private String role;
    private String status;

    public UserProperties() { }

    public UserProperties(String role, String status) {
        this.role = role;
        this.status = status;
    }

    public UserProperties(String email, String role, String status) {
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
