package com.pozdeev.HelloWorld.security;

import com.pozdeev.HelloWorld.exception.BlackListException;
import com.pozdeev.HelloWorld.exception.MismatchRefreshTokenException;
import com.pozdeev.HelloWorld.models.security.Permission;
import com.pozdeev.HelloWorld.models.security.Role;
import com.pozdeev.HelloWorld.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    private final static String AUTHENTICATION_SCHEME_JWT_TOKEN = "Bearer";
    //private final static  String AUTHENTICATION_SCHEME_JWT_REFRESH_TOKEN = "Bearer";
    private final static String HTTP_HEADER_REFRESH = "Refresh";

    private final JwtTokenProvider jwtTokenProvider;
    private final Memory memory;
    private boolean refreshFlag;

    @Autowired
    public JwtTokenPersistenceFilter(JwtTokenProvider jwtTokenProvider, Memory memory)
    {
        this.memory = memory;
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
        } catch (JwtException e) {
            LOGGER.info("IN doFilter(): wrong token structure");
        } catch (BlackListException e) {
            LOGGER.debug("IN doFilter(): BlackListException", e);
        } catch (MismatchRefreshTokenException e) {
            LOGGER.debug("IN doFilter(): MismatchRefreshTokenException", e);
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

    private Authentication getAuthenticationFromAccessToken(String token) throws JwtException, BlackListException {
        if(memory.accessTokenInBlackList(token)) {
            throw new BlackListException("Access deny because provide AccessToken in BlackList");
        }
        Claims accessClaims = jwtTokenProvider.getAccessClaims(token);
        String email = accessClaims.getSubject();
        String role = accessClaims.get("role", String.class);

        return new JwtUserAuthenticationToken(email, role, Role.valueOf(role).getAuthorities());
    }

    private Authentication getAuthenticationFromRefreshToken(String token) throws JwtException, MismatchRefreshTokenException{
        Claims accessClaims = jwtTokenProvider.getRefreshClaims(token);
        String email = accessClaims.getSubject();
        if (!memory.validateRefreshToken(email, token)) {
            throw new MismatchRefreshTokenException("Access deny because provide RefreshToken is not found in memory");
        }
        return new JwtUserAuthenticationToken(email, "",
                Set.of(new SimpleGrantedAuthority(Permission.TOKEN_REFRESH.getPermission())));
    }

}
