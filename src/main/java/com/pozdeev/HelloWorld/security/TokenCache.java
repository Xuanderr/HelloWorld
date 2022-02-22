package com.pozdeev.HelloWorld.security;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class TokenCache {

    private final Map<String, String> RefreshTokenAndUserCache = new HashMap<>();
    private final List<String> AccessTokenBlackList = new LinkedList<>();

    public String refreshTokenFromCache(String email) {
        return RefreshTokenAndUserCache.get(email);
    }

    public String addToRefreshStorage(String email, String refreshToken) {
        return RefreshTokenAndUserCache.put(email, refreshToken);
    }

    public boolean removeFromRefreshStorage(String email) {
        return RefreshTokenAndUserCache.remove(email) != null;
    }

    public boolean validateRefreshToken(String email, String refreshToken) {
        String saveRefreshToken = RefreshTokenAndUserCache.get(email);
        return saveRefreshToken.equals(refreshToken);
    }

    public boolean blackListContains(String accessToken) {
        return AccessTokenBlackList.contains(accessToken);
    }

    public boolean addToBlackList(String token) {
        return AccessTokenBlackList.add(token);
    }
}
