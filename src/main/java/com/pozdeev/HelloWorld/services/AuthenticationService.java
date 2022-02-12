package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.security.AuthenticationRequest;
import com.pozdeev.HelloWorld.models.security.AuthenticationResponse;
import com.pozdeev.HelloWorld.models.security.Role;
import com.pozdeev.HelloWorld.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class.getName());

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final Map<String, String> refreshStorage = new HashMap<>();

    private Role role;

    protected void setRole(Role role) {
        this.role = role;
    }

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private boolean validateRefreshToken(String email, String refreshToken) {
        String saveRefreshToken = refreshStorage.get(email);
        return saveRefreshToken.equals(refreshToken);
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            String accessToken = jwtTokenProvider.generateAccessToken(auth.getName(), role.name());
            String refreshToken = jwtTokenProvider.generateRefreshToken(auth.getName());
            refreshStorage.put(auth.getName(), refreshToken);
            return new AuthenticationResponse(accessToken, refreshToken);
        } catch (AuthenticationException ex) {
            LOGGER.debug("Authentication failed", ex);
        }
        return null;
    }


    public AuthenticationResponse refresh(String refreshToken) {
        try {
            String email = jwtTokenProvider.getRefreshClaims(refreshToken).getSubject();
            if (!validateRefreshToken(email, refreshToken)) {
                return null;
            }
            String newAccessToken = jwtTokenProvider.generateAccessToken(email, role.name());
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);
            refreshStorage.put(email, newRefreshToken);
            return new AuthenticationResponse(newAccessToken, newRefreshToken);
        } catch (AuthenticationException ex) {
            LOGGER.debug("Authentication failed", ex);
        }
        return null;
    }
}
