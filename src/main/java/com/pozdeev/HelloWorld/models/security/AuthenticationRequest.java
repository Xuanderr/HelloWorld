package com.pozdeev.HelloWorld.models.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationRequest {

    private String email;
    private String password;

    public AuthenticationRequest() { }

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
