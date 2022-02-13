package com.pozdeev.HelloWorld.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long articleId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "anons", nullable = false)
    private String anons;

    @Column(name = "full_text", nullable = false)
    private String fullText;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(name = "created_date_time", updatable = false)
    private LocalDateTime created;

    @Column(name = "likes")
    private int likes;

    @Column(name = "reposts")
    private int reposts;

    @Column(name = "views")
    private int views;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "article_tags",
            joinColumns = {@JoinColumn(name = "article_id", referencedColumnName = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    private Set<Tag> tags;


    public Article() {  }

    public Article(Long articleId, String title, String anons, String fullText, User author) {
        this.articleId = articleId;
        this.title = title;
        this.anons = anons;
        this.fullText = fullText;
        this.author = author;
    }

    public Article(Long articleId, String title, String anons, String fullText, User author, LocalDateTime created, int likes, int reposts, int views) {
        this.articleId = articleId;
        this.title = title;
        this.anons = anons;
        this.fullText = fullText;
        this.author = author;
        this.created = created;
        this.likes = likes;
        this.reposts = reposts;
        this.views = views;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getReposts() {
        return reposts;
    }

    public void setReposts(int reposts) {
        this.reposts = reposts;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void viewsIncrement() {
        this.views += 1;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", anons='" + anons + '\'' +
                ", fullText='" + fullText + '\'' +
                ", author=" + author +
                ", created=" + created +
                ", likes=" + likes +
                ", reposts=" + reposts +
                ", views=" + views +
                '}';
    }
}
