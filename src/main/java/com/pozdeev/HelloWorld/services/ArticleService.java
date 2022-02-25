package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.cache.LikeCache;
import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.repositories.ArticleRepo;
import com.pozdeev.HelloWorld.security.JwtTokenPersistenceFilter;
import org.hibernate.JDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ArticleService.class.getName());
    private final ArticleRepo articleRepo;

    @Autowired
    public ArticleService(ArticleRepo articleRepo) {
        this.articleRepo = articleRepo;
    }

    public Page<Article> allArticles(Pageable pageable) throws DataIntegrityViolationException {
        try {
            Page<Article> localPage = articleRepo.findAll(pageable);
            List<Article> articles = localPage.getContent();
            for(Article article: articles) {
                article.prepareToResponse();
                setLikesAmountForArticle(article);
            }
            return localPage;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN ArticleService.allArticles(): {}", e.getCause(), e);
            throw e;
        }
    }

    public Optional<Article> oneArticle(Long id) throws DataIntegrityViolationException {
        try {
            Optional<Article> updArticle = articleRepo.findById(id);
            if(updArticle.isEmpty()) {
                return Optional.empty();
            }
            updArticle.get().viewsIncrement();
            Article localArticle = articleRepo.save(updArticle.get());
            localArticle.prepareToResponse();
            setLikesAmountForArticle(localArticle);
            return Optional.of(localArticle);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN ArticleService.oneArticle(): {}", e.getCause(), e);
            throw e;
        }
    }

    public Page<Article> articlesByStr(String str, Pageable pageable) throws DataIntegrityViolationException {
        try {
            Page<Article> localPage = articleRepo.findByAnonsContaining(str, pageable);
            List<Article> articles = localPage.getContent();
            for(Article article: articles) {
                article.prepareToResponse();
                setLikesAmountForArticle(article);
            }
            return localPage;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN ArticleService.articlesByStr(): {}", e.getCause(), e);
            throw e;
        }

    }

    public Page<Article> articlesByTags(Pageable pageable, String... tags) throws DataIntegrityViolationException {
        try {
            Page<Article> localPage;
            if (tags.length == 1) {
                localPage = articleRepo.findByTag(tags[0], pageable);
                localPage.getContent().forEach(Article::prepareToResponse);
                return localPage;
            }
            localPage = articleRepo.findByTags(tags, pageable);
            List<Article> articles = localPage.getContent();
            for(Article article: articles) {
                article.prepareToResponse();
                setLikesAmountForArticle(article);
            }
            return localPage;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN ArticleService.articlesByTags(): {}", e.getCause(), e);
            throw e;
        }
    }

    public Optional<Article> createNewArticle(Article newArticle) throws DataIntegrityViolationException {
        try {
            Article localArticle = articleRepo.save(newArticle);
            localArticle.prepareToResponse();
            return Optional.of(localArticle);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN ArticleService.createNewArticle(): {}", e.getCause(), e);
            throw e;
        }
    }

    public Optional<Article> updateArticle(Long id, Article article) throws DataIntegrityViolationException {
        try {
            Optional<Article> updArticle = articleRepo.findById(id);
            if(updArticle.isEmpty()) {
                return Optional.empty();
            }
            updArticle.get().setTitle(article.getTitle());
            updArticle.get().setAnons(article.getAnons());
            updArticle.get().setFullText(article.getFullText());
            Article localArticle = articleRepo.save(updArticle.get());
            localArticle.prepareToResponse();
            setLikesAmountForArticle(localArticle);
            return Optional.of(localArticle);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN ArticleService.updateArticle(): {}", e.getCause(), e);
            throw e;
        }
    }

    public boolean deleteArticleById(Long id) throws DataIntegrityViolationException {
        try {
            if (!articleRepo.existsById(id)) {
                return false;
            }
            articleRepo.deleteById(id);
            LikeCache.removeFromLikeCache(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN ArticleService.deleteArticleById(): {}", e.getCause(), e);
            throw e;
        }
    }

    private void setLikesAmountForArticle(Article article) {
        Integer amount = LikeCache.getFromLikeCache(article.getArticleId());
        if (amount == null) {
            amount = articleRepo.getLikesAmountByArticle(article.getArticleId());
            if (amount == null) {
                amount = 0;
            }
            LikeCache.addOrRefreshLikeCache(article.getArticleId(), amount);
        }
        article.setLikeAmount(amount);
    }
}
