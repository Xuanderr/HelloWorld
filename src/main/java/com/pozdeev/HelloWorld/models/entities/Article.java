package com.pozdeev.HelloWorld.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", nullable = false)
    private Integer articleId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "anons", nullable = false)
    private String anons;

    @Column(name = "full_text", nullable = false)
    private String fullText;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public Article() {  }

    public Article(Integer articleId, String title, String anons, String fullText, User author) {
        this.articleId = articleId;
        this.title = title;
        this.anons = anons;
        this.fullText = fullText;
        this.author = author;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnons() {
        return anons;
    }

    public void setAnons(String anons) {
        this.anons = anons;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
