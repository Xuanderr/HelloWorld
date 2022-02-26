package com.pozdeev.HelloWorld.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pozdeev.HelloWorld.models.entities.user.SmallUser;
import com.pozdeev.HelloWorld.models.entities.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long articleId;

    @Column(name = "title")
    private String title;

    @Column(name = "anons")
    private String anons;

    @Column(name = "full_text")
    private String fullText;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User author;

    @Column(name = "created_date_time")
    private LocalDateTime created;

    @Column(name = "views")
    private int views;

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(name = "articles_tags",
            joinColumns = {@JoinColumn(name = "article_id", referencedColumnName = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_name", referencedColumnName = "name")})
    private Set<Tag> tags = new HashSet<>();

    @Transient
    private int likeAmount;

    public Article() {  }

    public Article(Long articleId) {
        this.articleId = articleId;
    }

    // Methods of synchronized bidirectional relationships
    public void addComment(Comment comment){
        this.comments.add(comment);
        comment.setArticle(this);
    }
    public void removeComment(Comment comment){
        this.comments.remove(comment);
        comment.setArticle(null);
    }

    public void addLike(Like like){
        this.likes.add(like);
        like.setArticle(this);
    }
    public void removeLike(Like like){
        this.likes.remove(like);
        like.setArticle(null);
    }

    public void addTag(Tag tag){
        this.tags.add(tag);
        tag.getArticles().add(this);
    }
    public void removeTag(Tag tag){
        this.tags.remove(tag);
        tag.getArticles().remove(this);
    }
    //-------------------------------------


    public void viewsIncrement() {
        this.views += 1;
    }

    public void prepareToResponse() {
        this.author = new SmallUser(this.author.getName(), this.author.getUserId());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return articleId.equals(article.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId);
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
