package com.pozdeev.HelloWorld.security;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class Memory {

    private final Map<String, String> refreshStorage = new HashMap<>();
    private final List<String> blackList = new LinkedList<>();

    public boolean addToBlackList(String token) {
        return blackList.add(token);
    }

    public String addToRefreshStorage(String email, String refreshToken) {
        return refreshStorage.put(email, refreshToken);
    }

    public boolean removeFromRefreshStorage(String email) {
        return refreshStorage.remove(email) != null;
    }

    public boolean validateRefreshToken(String email, String refreshToken) {
        String saveRefreshToken = refreshStorage.get(email);
        return saveRefreshToken.equals(refreshToken);
    }

    public boolean accessTokenInBlackList(String accessToken) {
        return blackList.contains(accessToken);
    }
}
