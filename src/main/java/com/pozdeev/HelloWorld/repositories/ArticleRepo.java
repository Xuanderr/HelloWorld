package com.pozdeev.HelloWorld.repositories;

import com.pozdeev.HelloWorld.models.entities.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ArticleRepo extends CrudRepository<Article, Long> {

    Page<Article> findAll(Pageable pageable);

    Page<Article> findByAnonsContaining(String str, Pageable pageable);

    @Query(value = """
            SELECT count(l.article_id) FROM articles a
            INNER JOIN likes l ON a.article_id=l.article_id
            WHERE a.article_id = :id
            GROUP BY a.article_id""", nativeQuery = true)
    Integer getLikesAmountByArticle(Long id);

    @Query(value = """
            SELECT a.article_id, a.title, a.anons, a.full_text, a.user_id, a.created_date_time, a.views
            FROM articles a
            INNER JOIN articles_tags at ON a.article_id = at.article_id
            INNER JOIN tags t ON at.tag_id = t.id
            WHERE t.user IN (:tags)
            GROUP BY a.article_id
            HAVING COUNT(*)>1""",
            countQuery = """
                    SELECT COUNT(*) FROM
                    (SELECT a.article_id, a.title, a.anons, a.full_text, a.user_id, a.created_date_time, a.views
                    FROM articles a
                    INNER JOIN articles_tags at ON a.article_id = at.article_id
                    INNER JOIN tags t ON at.tag_id = t.id
                    WHERE t.user IN (:tags)
                    GROUP BY a.article_id
                    HAVING COUNT(*)>1)""",
            nativeQuery = true)
    Page<Article> findByTags(String[] tags, Pageable pageable);

    @Query(value = """
            SELECT a.article_id, a.title, a.anons, a.full_text, a.user_id, a.created_date_time, a.views
            FROM articles a
            INNER JOIN articles_tags at ON a.article_id = at.article_id
            INNER JOIN tags t ON at.tag_id = t.id
            WHERE t.user = :tag""",
            countQuery = """
                    SELECT COUNT(*)
                    FROM articles a
                    INNER JOIN articles_tags at ON a.article_id = at.article_id
                    INNER JOIN tags t ON at.tag_id = t.id
                    WHERE t.user = :tag""",
            nativeQuery = true)
    Page<Article> findByTag(String tag, Pageable pageable);


}
