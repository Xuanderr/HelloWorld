package com.pozdeev.HelloWorld.cache;

import java.util.HashMap;
import java.util.Map;

public class LikeCache {

    private static final Map<Long, Integer> LikeAmountForArticle = new HashMap<>();

    public static Integer getFromLikeCache(Long id) {
        return LikeAmountForArticle.get(id);
    }

    public static void addOrRefreshLikeCache(Long id, Integer amount) {
        LikeAmountForArticle.put(id, amount);
    }

    public static void removeFromLikeCache(Long id) {
        LikeAmountForArticle.remove(id);
    }

}
