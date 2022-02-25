package com.pozdeev.HelloWorld.security;

import com.pozdeev.HelloWorld.cache.TokenCache;
import com.pozdeev.HelloWorld.exception.BlackListException;
import com.pozdeev.HelloWorld.exception.MismatchRefreshTokenException;
import com.pozdeev.HelloWorld.models.security.Permission;
import com.pozdeev.HelloWorld.models.security.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class JwtTokenPersistenceFilter extends GenericFilterBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtTokenPersistenceFilter.class.getName());
    private final static String AUTHENTICATION_SCHEME_JWT_TOKEN = "Bearer";
    private final static String HTTP_HEADER_REFRESH = "Refresh";


    private boolean refreshFlag;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        try {
            String token = getTokenFromRequest((HttpServletRequest) servletRequest);
            if(token == null) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            if(refreshFlag) {
                setRefreshFlag(false);
                Authentication authentication = getAuthenticationFromRefreshToken(token);
                injectAuthenticationIntoContext(authentication);

            } else {
                Authentication authentication = getAuthenticationFromAccessToken(token);
                injectAuthenticationIntoContext(authentication);
            }
            filterChain.doFilter(servletRequest, servletResponse);

        } catch (BadCredentialsException e) {
            LOGGER.info("IN JwtTokenPersistenceFilter.doFilter(): BadCredentialsException", e);
            ((HttpServletResponse) servletResponse)
                    .sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());

        } catch (JwtException e) {
            LOGGER.info("IN JwtTokenPersistenceFilter.doFilter(): wrong token structure", e.getCause());
            ((HttpServletResponse) servletResponse)
                    .sendError(HttpStatus.UNAUTHORIZED.value(), "Wrong token structure");

        } catch (BlackListException e) {
            LOGGER.info("IN JwtTokenPersistenceFilter.doFilter(): BlackListException", e);
            ((HttpServletResponse) servletResponse)
                    .sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());

        } catch (MismatchRefreshTokenException e) {
            LOGGER.info("IN JwtTokenPersistenceFilter.doFilter(): MismatchRefreshTokenException", e);
            ((HttpServletResponse) servletResponse)
                    .sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String result = getAccessTokenFromRequest(request);
        return result == null
                ? getRefreshTokenFromRequest(request)
                : result;
    }

    private String getAccessTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null) {
            LOGGER.debug("IN JwtTokenPersistenceFilter.getAccessTokenFromRequest(): Header Authorization is empty");
            return null;
        }
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_JWT_TOKEN)) {
            LOGGER.debug(
                    "IN JwtTokenPersistenceFilter.getAccessTokenFromRequest(): Mismatch with AUTHENTICATION_SCHEME_JWT_TOKEN");
            return null;
        }
        if (header.equalsIgnoreCase(AUTHENTICATION_SCHEME_JWT_TOKEN)) {
            throw new BadCredentialsException("Empty JWT Access token");
        }
        return header.substring(7);
    }

    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HTTP_HEADER_REFRESH);
        if (header == null) {
            LOGGER.debug("IN JwtTokenPersistenceFilter.getAccessTokenFromRequest(): Header Refresh is empty");
            return null;
        }
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_JWT_TOKEN)) {
            LOGGER.debug(
                    "IN JwtTokenPersistenceFilter.getRefreshTokenFromRequest(): Mismatch with AUTHENTICATION_SCHEME_JWT_TOKEN");
            return null;
        }
        if (header.equalsIgnoreCase(AUTHENTICATION_SCHEME_JWT_TOKEN)) {
            throw new BadCredentialsException("Empty JWT Refresh token");
        }
        setRefreshFlag(true);
        return header.substring(7);
    }

    private void setRefreshFlag(boolean refreshFlag) {
        this.refreshFlag = refreshFlag;
    }

    private void injectAuthenticationIntoContext(Authentication authentication) {
        authentication.setAuthenticated(true);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private Authentication getAuthenticationFromAccessToken(String token) throws JwtException, BlackListException {
        if(TokenCache.blackListContains(token)) {
            throw new BlackListException("AccessToken in BlackList");
        }
        Claims accessClaims = JwtTokenProvider.getAccessClaims(token);
        String id = accessClaims.getSubject();
        String role = accessClaims.get("role", String.class);

        return new JwtUserAuthenticationToken(Long.valueOf(id), Role.valueOf(role).getAuthorities());
    }

    private Authentication getAuthenticationFromRefreshToken(String token) throws JwtException, MismatchRefreshTokenException{
        Long id = Long.valueOf(JwtTokenProvider.getRefreshClaims(token).getSubject());
        if (!TokenCache.validateRefreshToken(id, token)) {
            throw new MismatchRefreshTokenException("RefreshToken is not found in storage");
        }
        return new JwtUserAuthenticationToken(id,
                Set.of(new SimpleGrantedAuthority(Permission.TOKEN_REFRESH.getPermission())));
    }

}
