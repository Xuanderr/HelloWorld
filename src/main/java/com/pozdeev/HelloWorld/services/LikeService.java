package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.Like;
import com.pozdeev.HelloWorld.repositories.LikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepo likeRepo;

    @Autowired
    public LikeService(LikeRepo likeRepo) {
        this.likeRepo = likeRepo;
    }

    public Like addLike(Like like) {
        return likeRepo.save(like);
    }

    @Transactional
    public boolean removeLike(Like like) {
        if (!likeRepo.existsByUserAndArticle(like.getUser(), like.getArticle())) {
            return false;
        }
        likeRepo.deleteByUserAndArticle(like.getUser(), like.getArticle());
        return true;
    }
}
