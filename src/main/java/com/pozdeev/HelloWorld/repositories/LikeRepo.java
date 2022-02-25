package com.pozdeev.HelloWorld.repositories;

import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.models.entities.Like;
import com.pozdeev.HelloWorld.models.entities.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepo extends CrudRepository<Like, Long> {

    void deleteByUserAndArticle(User user, Article article);

    boolean existsByUserAndArticle(User user, Article article);
}
