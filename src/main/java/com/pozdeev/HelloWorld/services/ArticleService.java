package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.repositories.ArticleRepo;
import com.pozdeev.HelloWorld.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepo articleRepo;

    @Autowired
    public ArticleService(ArticleRepo articleRepo) {
        this.articleRepo = articleRepo;
    }

    public Page<Article> allArticles(Pageable pageable) {
        return articleRepo.findAll(pageable);
    }

    public Article oneArticle(Long id) {
        Optional<Article> updArticle = articleRepo.findById(id);
        if(updArticle.isEmpty()) {
            return null;
        }
        updArticle.get().viewsIncrement();
        return articleRepo.save(updArticle.get());
    }

    public Article saveCurrentLikes(Long id, int likes) {
        Optional<Article> updArticle = articleRepo.findById(id);
        if(updArticle.isEmpty()) {
            return null;
        }
        return articleRepo.save(updArticle.get());
    }

    public Article createNewArticle(Article newArticle) {
        newArticle.setCreated(LocalDateTime.now());
        newArticle.setViews(0);
        return articleRepo.save(newArticle);
    }

    public Article updateArticle(Article article) {
        Optional<Article> updArticle = articleRepo.findById(article.getArticleId());
        if(updArticle.isEmpty()) {
            return null;
        }
        updArticle.get().setTitle(article.getTitle());
        updArticle.get().setAnons(article.getAnons());
        updArticle.get().setFullText(article.getFullText());
        return articleRepo.save(updArticle.get());
    }

    public boolean deleteArticleById(Long id) {
        if (!articleRepo.existsById(id)) {
            return false;
        }
        articleRepo.deleteById(id);
        return true;
    }

    public Page<Article> oneArticleByStr(String str, Pageable pageable) {
        return articleRepo.findByAnonsContaining(str, pageable);
    }


    public Page<Article> articlesByTags(String[] tags, Pageable pageable) {
        if (tags.length == 1) {
            return articleRepo.findByTag(tags[0], pageable);
        }
        return articleRepo.findByTags(tags, pageable);
    }

}
