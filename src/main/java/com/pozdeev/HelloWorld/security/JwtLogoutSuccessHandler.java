package com.pozdeev.HelloWorld.security;

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
    //private static final String AUTHENTICATION_SCHEME_JWT_REFRESH_TOKEN = "Bearer";
    private final static String HTTP_HEADER_REFRESH = "Refresh";

    private JwtTokenProvider jwtTokenProvider;
    private TokenCache tokenCache;

    private boolean refreshFlag;

    @Autowired
    public void setMemory(TokenCache tokenCache) {
        this.tokenCache = tokenCache;
    }

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            String token = getTokenFromRequest(request);
            if(token == null) {
                LOGGER.info("IN onLogoutSuccess(): wrong request header with token");
                response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
                return;
            }
            if(refreshFlag) {
                setRefreshFlag(false);
                String subject = jwtTokenProvider.getRefreshClaims(token).getSubject();
                if (!tokenCache.validateRefreshToken(subject, token)) {
                    LOGGER.info("IN onLogoutSuccess(): mismatch provide token with saved token");
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
                    return;
                }
                if(!tokenCache.removeFromRefreshStorage(subject)) {
                    LOGGER.debug("Deleting element from refreshStorage is failed");
                }

            } else {
                String subject = jwtTokenProvider.getAccessClaims(token).getSubject();
                if(!tokenCache.addToBlackList(token)) {
                    LOGGER.debug("Adding element to blackList is failed");
                }
                if(!tokenCache.removeFromRefreshStorage(subject)) {
                    LOGGER.debug("Deleting element from refreshStorage is failed");
                }
            }
            response.setStatus(HttpStatus.NO_CONTENT.value());
            response.getWriter().flush();
        } catch (BadCredentialsException e) {
        LOGGER.debug("IN onLogoutSuccess(): BadCredentialsException", e);
        } catch (JwtException e) {
        LOGGER.info("IN onLogoutSuccess(): error in token structure");
        }
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
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

}
