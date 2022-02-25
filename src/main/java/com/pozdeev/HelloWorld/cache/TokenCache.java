package com.pozdeev.HelloWorld.cache;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenCache {

    private static final Map<Long, String> RefreshTokenAndUserCache = new HashMap<>();
    private static final Set<String> AccessTokenBlackList = new TreeSet<>();

    public static String refreshTokenFromStorage(Long id) {
        return RefreshTokenAndUserCache.get(id);
    }

    public static void addToRefreshStorage(Long id, String refreshToken) {
        RefreshTokenAndUserCache.put(id, refreshToken);
    }

    public static boolean removeFromRefreshStorage(Long id) {
        return RefreshTokenAndUserCache.remove(id) != null;
    }

    public static boolean validateRefreshToken(Long id, String refreshToken) {
        String saveRefreshToken = RefreshTokenAndUserCache.get(id);
        return saveRefreshToken.equals(refreshToken);
    }

    public static boolean blackListContains(String accessToken) {
        return AccessTokenBlackList.contains(accessToken);
    }

    public static boolean addToBlackList(String token) {
        return AccessTokenBlackList.add(token);
    }
}
