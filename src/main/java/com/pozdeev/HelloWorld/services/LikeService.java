package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.cache.LikeCache;
import com.pozdeev.HelloWorld.models.entities.Like;
import com.pozdeev.HelloWorld.repositories.LikeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final static Logger LOGGER = LoggerFactory.getLogger(LikeService.class.getName());
    private final LikeRepo likeRepo;

    @Autowired
    public LikeService(LikeRepo likeRepo) {
        this.likeRepo = likeRepo;
    }

    public boolean putLike(Like like) throws DataIntegrityViolationException {
        try {
            if(likeRepo.existsByUserAndArticle(like.getUser(), like.getArticle())) {
                return false;
            }
            likeRepo.save(like);
            LikeCache.addOrRefreshLikeCache(like.getArticle().getArticleId(), like.getLikesAmount());
            return true;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN LikeService.putLike(): {}", e.getCause(), e);
            throw e;
        }
    }

    @Transactional
    public boolean removeLike(Like like) throws DataIntegrityViolationException {
        try {
            if (!likeRepo.existsByUserAndArticle(like.getUser(), like.getArticle())) {
                return false;
            }
            likeRepo.deleteByUserAndArticle(like.getUser(), like.getArticle());
            LikeCache.addOrRefreshLikeCache(like.getArticle().getArticleId(), like.getLikesAmount());
            return true;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN LikeService.removeLike(): {}", e.getCause(), e);
            throw e;
        }
    }
}
