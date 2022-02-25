package com.pozdeev.HelloWorld.security;

import com.pozdeev.HelloWorld.cache.TokenCache;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtLogoutSuccessHandler.class.getName());

    private final static String AUTHENTICATION_SCHEME_JWT_TOKEN = "Bearer";
    private final static String HTTP_HEADER_REFRESH = "Refresh";

    private boolean refreshFlag;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            String token = getTokenFromRequest(request);
            if(token == null) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "ERROR with toke header");
                return;
            }
            if(refreshFlag) {
                setRefreshFlag(false);
                Long subject = Long.valueOf(JwtTokenProvider.getRefreshClaims(token).getSubject());
                if (!TokenCache.validateRefreshToken(subject, token)) {
                    LOGGER.debug("IN JwtLogoutSuccessHandler.onLogoutSuccess(): mismatch provide token with saved token");
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
                    return;
                }
                if(!TokenCache.removeFromRefreshStorage(subject)) {
                    LOGGER.debug("Deleting element from refreshStorage is failed");
                }

            } else {
                Long subject = Long.valueOf(JwtTokenProvider.getAccessClaims(token).getSubject());
                if(!TokenCache.addToBlackList(token)) {
                    LOGGER.debug("Token already in BlackList");
                }
                if(!TokenCache.removeFromRefreshStorage(subject)) {
                    LOGGER.debug("See method HashMap.remove");
                }
            }
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.getWriter().flush();
        } catch (BadCredentialsException e) {
        LOGGER.info("IN JwtLogoutSuccessHandler.onLogoutSuccess(): BadCredentialsException", e);
            response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());

        } catch (JwtException e) {
        LOGGER.info("IN JwtLogoutSuccessHandler.onLogoutSuccess(): wrong token structure", e.getCause());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Wrong token structure");
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
            LOGGER.debug("IN JwtLogoutSuccessHandler.getAccessTokenFromRequest(): Header Authorization is empty");
            return null;
        }
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_JWT_TOKEN)) {
            LOGGER.debug(
                    "IN JwtLogoutSuccessHandler.getAccessTokenFromRequest(): Mismatch with AUTHENTICATION_SCHEME_JWT_TOKEN");
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
            LOGGER.debug("IN JwtLogoutSuccessHandler.getRefreshTokenFromRequest(): Header Refresh is empty");
            return null;
        }
        header = header.trim();
        if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_JWT_TOKEN)) {
            LOGGER.debug(
                    "IN JwtLogoutSuccessHandler.getRefreshTokenFromRequest(): Mismatch with AUTHENTICATION_SCHEME_JWT_TOKEN");
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

}
