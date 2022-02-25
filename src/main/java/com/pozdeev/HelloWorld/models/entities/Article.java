package com.pozdeev.HelloWorld.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pozdeev.HelloWorld.models.entities.user.SmallUser;
import com.pozdeev.HelloWorld.models.entities.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "articles")
public class Article {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long articleId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "title", nullable = false)
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "anons", nullable = false)
    private String anons;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "full_text", nullable = false)
    private String fullText;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(name = "created_date_time", nullable = false)
    private LocalDateTime created;

    @Column(name = "views")
    private int views;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "articles_tags",
            joinColumns = {@JoinColumn(name = "article_id", referencedColumnName = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_name", referencedColumnName = "name")})
    private Set<Tag> tags;

    @Transient
    private int likeAmount;

    public Article() {  }

    public Article(Long articleId) {
        this.articleId = articleId;
    }

    public Article(String title, String anons, String fullText) {
        this.title = title;
        this.anons = anons;
        this.fullText = fullText;
    }

    public Article(String title, String anons, String fullText, User author) {
        this.title = title;
        this.anons = anons;
        this.fullText = fullText;
        this.author = author;
        this.created = LocalDateTime.now();
        this.views = 0;
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

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public int getLikeAmount() {
        return likeAmount;
    }

    public void setLikeAmount(int likeAmount) {
        this.likeAmount = likeAmount;
    }

    public void viewsIncrement() {
        this.views += 1;
    }

    public void prepareToResponse() {
        this.author =  new SmallUser(this.author.getName());
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
                ", views=" + views +
                '}';
    }
}
