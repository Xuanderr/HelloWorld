package com.pozdeev.HelloWorld.security;

import io.jsonwebtoken.*;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class.getName());

    private static String accessSecretKey = RandomStringUtils.randomAlphanumeric(15);

    private static String refreshSecretKey = RandomStringUtils.randomAlphanumeric(15);;

    @PostConstruct
    private void init() {
        accessSecretKey = Base64.getEncoder().encodeToString(accessSecretKey.getBytes());
        refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
    }

    public static String generateAccessToken(Long id, String role) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime expiration = now.plusMinutes(10);

        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, accessSecretKey)
                .claim("role", role)
                .compact();
    }

    public static String generateRefreshToken(Long id, String role) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime expiration = now.plusDays(30);

        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .claim("role", role)
                .compact();
    }

    public static Claims getAccessClaims(String token) throws JwtException {
        return getClaims(token, accessSecretKey);
    }

    public static Claims getRefreshClaims(String token) throws JwtException {
        return getClaims(token, refreshSecretKey);
    }

    private static Claims getClaims(String token, String secret) {
        // Also, this method do checking of validation TokenStructure
        try {
            return Jwts.parser().
                    setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch(UnsupportedJwtException e) {
            LOGGER.debug("IN JwtTokenProvider.getClaims(): UnsupportedJwtException", e);
            throw e;
        } catch (MalformedJwtException e) {
            LOGGER.debug("IN JwtTokenProvider.getClaims(): MalformedJwtException", e);
            throw e;
        } catch (SignatureException e) {
            LOGGER.debug("IN JwtTokenProvider.getClaims(): SignatureException", e);
            throw e;
        } catch (ExpiredJwtException e) {
            LOGGER.debug("IN JwtTokenProvider.getClaims(): ExpiredJwtException", e);
            throw e;
        } catch (IllegalArgumentException e) {
            LOGGER.debug("IN JwtTokenProvider.getClaims(): IllegalArgumentException", e);
            throw e;
        }
    }
}
