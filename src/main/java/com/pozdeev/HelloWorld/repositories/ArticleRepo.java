package com.pozdeev.HelloWorld.repositories;

import com.pozdeev.HelloWorld.models.entities.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface ArticleRepo extends CrudRepository<Article, Long> {

    Page<Article> findAll(Pageable pageable);

    Page<Article> findByAnonsContaining(String str, Pageable pageable);

    int countAllByAuthor(Long id);

    @Query(value = "SELECT a.article_id, a.title, a.anons, a.full_text, a.user_id, a.created_date_time, a.likes, a.reposts, a.views" +
            " FROM articles a INNER JOIN article_tags at ON a.article_id=at.article_id INNER JOIN tags t ON t.id=at.tag_id " +
            "WHERE t.name = ?1",
            countQuery = "SELECT COUNT(a.article_id) FROM articles a INNER JOIN article_tags at ON a.article_id=at.article_id" +
                    " INNER JOIN tags t ON t.id=at.tag_id WHERE t.name = ?1",
            nativeQuery = true)
    Page<Article> findByTag(Collection<String> tags, Pageable pageable);


}
