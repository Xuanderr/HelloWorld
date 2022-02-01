package com.pozdeev.HelloWorld.repositories;

import com.pozdeev.HelloWorld.models.entities.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepo extends CrudRepository<Article, Integer> {

    List<Article> findAllByOrderByArticleIdDesc();
}
