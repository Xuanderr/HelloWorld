package com.pozdeev.HelloWorld.models.entities;

import com.pozdeev.HelloWorld.models.entities.user.User;

import javax.persistence.*;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long likeId;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne()
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Transient
    private Integer likesAmount;

    public Like() { }

    public Like(User user, Article article, Integer likesAmount) {
        this.user = user;
        this.article = article;
        this.likesAmount = likesAmount;
    }

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Integer getLikesAmount() {
        return likesAmount;
    }

    public void setLikesAmount(Integer likesAmount) {
        this.likesAmount = likesAmount;
    }
}
