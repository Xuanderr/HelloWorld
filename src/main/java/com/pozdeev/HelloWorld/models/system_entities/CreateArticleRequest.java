package com.pozdeev.HelloWorld.models.system_entities;

import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.models.entities.Tag;
import com.pozdeev.HelloWorld.models.entities.user.User;

import java.util.ArrayList;
import java.util.List;

public class CreateArticleRequest {

    private Article article;

    private User author;

    private List<Tag> tags = new ArrayList<>();

    public CreateArticleRequest() { }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
