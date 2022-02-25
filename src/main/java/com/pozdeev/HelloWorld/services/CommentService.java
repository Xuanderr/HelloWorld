package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.models.entities.Comment;
import com.pozdeev.HelloWorld.repositories.CommentRepo;
import org.hibernate.JDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class CommentService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommentService.class.getName());
    private final CommentRepo commentRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public Page<Comment> commentsByArticle(Long id, Pageable pageable) throws DataIntegrityViolationException {
        try {
            Article article = new Article(id);
            return commentRepo.findByArticle(article, pageable);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN CommentService.commentsByArticle(): {}", e.getCause(), e);
            throw e;
        }

    }

    public Optional<Comment> createNewComment(Comment newComment) throws DataIntegrityViolationException {
        try {
            newComment.setCreated(LocalDateTime.now());
            return Optional.of(commentRepo.save(newComment));
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN CommentService.createNewComment(): {}", e.getCause(), e);
            throw e;
        }

    }

    public Optional<Comment> updateComment(Long id, Comment comment) throws DataIntegrityViolationException {
        try {
            Optional<Comment> updComment = commentRepo.findById(id);
            if(updComment.isEmpty()) {
                return Optional.empty();
            }
            updComment.get().setFullText(comment.getFullText());
            return Optional.of(commentRepo.save(updComment.get()));
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN CommentService.updateComment(): {}", e.getCause(), e);
            throw e;
        }
    }

    public boolean deleteCommentById(Long id) throws DataIntegrityViolationException {
        try {
            if (!commentRepo.existsById(id)) {
                return false;
            }
            commentRepo.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN CommentService.deleteCommentById(): {}", e.getCause(), e);
            throw e;
        }

    }

}
