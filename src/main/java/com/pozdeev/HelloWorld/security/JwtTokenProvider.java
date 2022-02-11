package com.pozdeev.HelloWorld.security;

import com.pozdeev.HelloWorld.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class.getName());

    @Value("${jwt.token.access.secret}")
    private String accessSecretKey;

    @Value("${jwt.token.refresh.secret}")
    private String refreshSecretKey;

    @PostConstruct
    protected void init() {
        accessSecretKey = Base64.getEncoder().encodeToString(accessSecretKey.getBytes());
        refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
    }

    public String generateAccessToken(String email, String role) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime expiration = now.plusMinutes(10);

        LOGGER.info("IN generateAccessToken(): Before generate");
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, accessSecretKey)
                .claim("role", role)
                .compact();
    }

    public String generateRefreshToken(String email) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime expiration = now.plusDays(30);

        LOGGER.info("IN generateRefreshToken(): Before generate");
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();
    }

    public boolean validateAccessTokenStructure(String token) throws JwtAuthenticationException {
        LOGGER.info("IN validateAccessTokenStructure()");
        return validateTokenStructure(token, accessSecretKey);
    }

    public boolean validateRefreshTokenStructure(String token) {
        LOGGER.info("IN validateRefreshTokenStructure()");
        return validateTokenStructure(token, refreshSecretKey);
    }

    private boolean validateTokenStructure(String token, String secretKey) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch(UnsupportedJwtException e) {
            LOGGER.info("IN validateTokenStructure(): UnsupportedJwtException");
        } catch (MalformedJwtException e) {
            LOGGER.info("IN validateTokenStructure(): MalformedJwtException");
        } catch (SignatureException e) {
            LOGGER.info("IN validateTokenStructure(): SignatureException");
        } catch (ExpiredJwtException e) {
            LOGGER.info("IN validateTokenStructure(): ExpiredJwtException");
        } catch (IllegalArgumentException e) {
            LOGGER.info("IN validateTokenStructure(): IllegalArgumentException");
        }
        return false;
    }

    public Claims getAccessClaims(String token) {
        return getClaims(token, accessSecretKey);
    }

    public Claims getRefreshClaims(String token) {
        return getClaims(token, refreshSecretKey);
    }

    private Claims getClaims(String token, String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
