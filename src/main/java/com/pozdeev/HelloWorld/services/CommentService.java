package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.Comment;
import com.pozdeev.HelloWorld.repositories.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepo commentRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo) {
        this.commentRepo = commentRepo;
    }

    public Page<Comment> allComments(Pageable pageable) {
        return commentRepo.findAll(pageable);
    }

    public Comment createNewComment(Comment newComment) {
        newComment.setCreated(LocalDateTime.now());
        return commentRepo.save(newComment);
    }

    public Comment updateComment(Comment comment) {
        Optional<Comment> updComment = commentRepo.findById(comment.getCommentId());
        if(updComment.isEmpty()) {
            return null;
        }
        updComment.get().setFullText(comment.getFullText());
        return commentRepo.save(updComment.get());
    }

    public boolean deleteCommentById(Long id) {
        if (!commentRepo.existsById(id)) {
            return false;
        }
        commentRepo.deleteById(id);
        return true;
    }

}
