package com.pozdeev.HelloWorld.security;

import com.pozdeev.HelloWorld.models.security.Permission;
import com.pozdeev.HelloWorld.models.security.Role;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

@Component
public class JwtTokenPersistenceFilter extends GenericFilterBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtTokenPersistenceFilter.class.getName());
    private static final String AUTHENTICATION_SCHEME_JWT_TOKEN = "Bearer";
    //private static final String AUTHENTICATION_SCHEME_JWT_REFRESH_TOKEN = "Bearer";
    private static final String HTTP_HEADER_REFRESH = "Refresh";

    private final JwtTokenProvider jwtTokenProvider;
    private boolean refreshFlag;

    @Autowired
    public JwtTokenPersistenceFilter(JwtTokenProvider jwtTokenProvider)
    {
        this.jwtTokenProvider = jwtTokenProvider;
    }

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
            return;

        } catch (BadCredentialsException e) {
            LOGGER.debug("IN doFilter(): BadCredentialsException", e);
        } catch (AuthenticationException e) {
            LOGGER.debug("IN doFilter(): AuthenticationException", e);
        }
        ((HttpServletResponse) servletResponse)
                .sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
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
            return null;
        }
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_JWT_TOKEN)) {
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
            return null;
        }
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_JWT_TOKEN)) {
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

    private Authentication getAuthenticationFromAccessToken(String token) {
        Claims accessClaims = jwtTokenProvider.getAccessClaims(token);
        String email = accessClaims.getSubject();
        String role = accessClaims.get("role", String.class);

        return new JwtUserAuthenticationToken(email, role, Role.valueOf(role).getAuthorities());
    }

    private Authentication getAuthenticationFromRefreshToken(String token) {
        Claims accessClaims = jwtTokenProvider.getRefreshClaims(token);
        String email = accessClaims.getSubject();
        return new JwtUserAuthenticationToken(email, "",
                Set.of(new SimpleGrantedAuthority(Permission.TOKEN_REFRESH.getPermission())));
    }

}
