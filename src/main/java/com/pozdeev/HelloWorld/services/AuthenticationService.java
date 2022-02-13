package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.User;
import com.pozdeev.HelloWorld.models.system_entities.AuthenticationRequest;
import com.pozdeev.HelloWorld.models.system_entities.AuthenticationResponse;
import com.pozdeev.HelloWorld.models.system_entities.LoginResponse;
import com.pozdeev.HelloWorld.models.security.Role;
import com.pozdeev.HelloWorld.security.JwtTokenProvider;
import com.pozdeev.HelloWorld.security.Memory;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class.getName());

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final Memory memory;

    private Role role;
    private User user;

    protected void setRole(Role role) {
        this.role = role;
    }

    protected void setUser(User user) {
        this.user = user;
    }

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, Memory memory) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.memory = memory;
    }

    public boolean login(AuthenticationRequest request, LoginResponse loginResponse) {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            String accessToken = jwtTokenProvider.generateAccessToken(auth.getName(), role.name());
            String refreshToken = jwtTokenProvider.generateRefreshToken(auth.getName());
            memory.addToRefreshStorage(auth.getName(), refreshToken);
            loginResponse.setAccessToken(accessToken);
            loginResponse.setRefreshToken(refreshToken);
            loginResponse.setUser(user);
            return true;
        } catch (AuthenticationException ex) {
            LOGGER.info("Authentication failed");
        }
        return false;
    }

    public AuthenticationResponse refresh(String refreshToken) {
        try {
            String email = jwtTokenProvider.getRefreshClaims(refreshToken).getSubject();
            if (!memory.validateRefreshToken(email, refreshToken)) {
                return null;
            }
            String newAccessToken = jwtTokenProvider.generateAccessToken(email, role.name());
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);
            memory.addToRefreshStorage(email, newRefreshToken);
            return new AuthenticationResponse(newAccessToken, newRefreshToken);
        } catch (JwtException ex) {
            LOGGER.info("Authentication failed");
        }
        return null;
    }
}
