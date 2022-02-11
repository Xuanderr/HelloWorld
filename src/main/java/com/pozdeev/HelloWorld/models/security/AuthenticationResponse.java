package com.pozdeev.HelloWorld.models.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;

    public AuthenticationResponse() { }

    public AuthenticationResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
