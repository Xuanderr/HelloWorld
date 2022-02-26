package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.exception.GenerationTokenException;
import com.pozdeev.HelloWorld.models.entities.user.User;
import com.pozdeev.HelloWorld.models.system_entities.AuthenticationRequest;
import com.pozdeev.HelloWorld.models.system_entities.AuthenticationResponse;
import com.pozdeev.HelloWorld.models.system_entities.LoginResponse;
import com.pozdeev.HelloWorld.security.JwtTokenProvider;
import com.pozdeev.HelloWorld.cache.TokenCache;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class.getName());

    private final AuthenticationManager authenticationManager;

    private User user;

    protected void setUser(User user) {
        this.user = user;
    }

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public boolean login(AuthenticationRequest request, LoginResponse loginResponse) throws GenerationTokenException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            String accessToken = JwtTokenProvider.generateAccessToken(user.getUserId(), user.getRole().name());
            String refreshToken = TokenCache.refreshTokenFromStorage(user.getUserId());
            if(refreshToken == null) {
                refreshToken = JwtTokenProvider.generateRefreshToken(user.getUserId(), user.getRole().name());
                if(refreshToken == null | accessToken == null) {
                    LOGGER.debug("IN AuthenticationService.login(): Generation Token ERROR");
                    throw new GenerationTokenException("Generation Token ERROR");
                }
                TokenCache.addToRefreshStorage(user.getUserId(), refreshToken);
            }
            loginResponse.setAccessToken(accessToken);
            loginResponse.setRefreshToken(refreshToken);
            loginResponse.setUser(user);
            return true;
        } catch (AuthenticationException e) {
            LOGGER.debug("IN AuthenticationService.login(): {}", e.getCause(), e);
            return false;
        }
    }

    public AuthenticationResponse refresh(String refreshToken) throws GenerationTokenException {
        try {
            Claims refreshClaims = JwtTokenProvider.getRefreshClaims(refreshToken);
            Long id = Long.valueOf(refreshClaims.getSubject());
            String role = refreshClaims.get("role", String.class);
            if (!TokenCache.validateRefreshToken(id, refreshToken)) {
                return null;
            }
            String newAccessToken = JwtTokenProvider.generateAccessToken(id, role);
            String newRefreshToken = JwtTokenProvider.generateRefreshToken(id, role);
            if(newRefreshToken == null | newAccessToken == null) {
                LOGGER.debug("IN AuthenticationService.refresh(): Generation Token ERROR");
                throw new GenerationTokenException("Generation Token ERROR");
            }
            TokenCache.addToRefreshStorage(id, newRefreshToken);
            return new AuthenticationResponse(newAccessToken, newRefreshToken);
        } catch (JwtException e) {
            LOGGER.info(
                    "IN AuthenticationService.refresh(): Authentication failed - Invalid RefreshToken Structure", e.getCause());
            return null;
        }
    }

}
