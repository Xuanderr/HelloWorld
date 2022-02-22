package com.pozdeev.HelloWorld.models.entities;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne()
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(name = "full_text", nullable = false)
    private String fullText;

    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime created;

    public Comment() {  }

    public Comment(Long commentId, Article article, User author, String fullText, LocalDateTime created) {
        this.commentId = commentId;
        this.article = article;
        this.author = author;
        this.fullText = fullText;
        this.created =created;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

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

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
