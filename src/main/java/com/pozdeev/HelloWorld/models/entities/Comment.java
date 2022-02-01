package com.pozdeev.HelloWorld.models.entities;


import javax.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Integer commentId;

    @ManyToOne()
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Column(name = "full_text", nullable = false)
    private String fullText;

    public Comment() {  }

    public Comment(Integer commentId, Article article, User author, String fullText) {
        this.commentId = commentId;
        this.article = article;
        this.author = author;
        this.fullText = fullText;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
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
}
