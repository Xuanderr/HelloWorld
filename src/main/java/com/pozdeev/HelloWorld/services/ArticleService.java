package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.repositories.ArticleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
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
        updArticle.get().setLikes(likes);
        return articleRepo.save(updArticle.get());
    }

    public Article createNewArticle(Article newArticle) {
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

    //сейчас не используется
    public int getArticlesAmountForUser(Long userId) {
        return articleRepo.countAllByAuthor(userId);
    }

    //сейчас не используется
    public Page<Article> getArticlesForTag(Collection<String> tags, Pageable pageable) {
        return articleRepo.findByTag(tags, pageable);
    }
}
