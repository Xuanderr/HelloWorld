package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.repositories.ArticleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class ArticleService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ArticleService.class.getName());

    private final ArticleRepo articleRepo;

    @Autowired
    public ArticleService(ArticleRepo articleRepo) {
        this.articleRepo = articleRepo;
    }

    public Page<Article> getAll(Pageable pageable) {
        LOGGER.info("IN getAll(Pageable pageable)");
        return articleRepo.findAll(pageable);
    }

    public Page<Article> getArticleForStr(String str, Pageable pageable) {
        LOGGER.info("IN getArticleForStr(String str, Pageable pageable)");
        return articleRepo.findByAnonsContaining(str, pageable);
    }

    public Article createNewArticle(Article newArticle) {
        LOGGER.info("IN createNewArticle(Article newArticle)");
        newArticle.setCreated(LocalDateTime.now());
        newArticle.setLikes(0);
        newArticle.setReposts(0);
        newArticle.setViews(0);
        return articleRepo.save(newArticle);
    }

    public Article updateArticle(Article article) {
        Optional<Article> updArticle = articleRepo.findById(article.getArticleId());
        if(updArticle.isEmpty()) {
            return null;
        }
        LOGGER.info("IN updateArticle(): After success find article in DB");
        updArticle.get().setTitle(article.getTitle());
        updArticle.get().setAnons(article.getAnons());
        updArticle.get().setFullText(article.getFullText());
        return articleRepo.save(updArticle.get());
    }

    public Optional<Article> getOneArticle(Long id) {
        LOGGER.info("IN getOneArticle(int id)");
        return articleRepo.findById(id);
    }

    public int getArticlesAmountForUser(Long userId) {
        LOGGER.info("IN getArticlesAmountForUser(int userId)");
        return articleRepo.countAllByAuthor(userId);
    }

    public Page<Article> getArticlesForTag(Collection<String> tags, Pageable pageable) {
        LOGGER.info("IN getArticlesForTag(String tag, Pageable pageable)");
        return articleRepo.findByTag(tags, pageable);
    }
}
