package com.pozdeev.HelloWorld.controllers;

import com.pozdeev.HelloWorld.models.entities.Comment;
import com.pozdeev.HelloWorld.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/api/v1/blog/comments/{id}")
    public ResponseEntity<Page<Comment>> getAllByArticle(@PathVariable(name = "id") Long id,
            @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<Comment> page = commentService.commentsByArticle(id, pageable);
        return !page.hasContent()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping(path = "/api/v1/blog/comment" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> createComment(@RequestBody Comment newComment) {
        Optional<Comment> comment = commentService.createNewComment(newComment);
        return comment.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(comment.get(), HttpStatus.OK);
    }

    @PutMapping(path = "/api/v1/blog/comment/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> updateComment(@PathVariable(name = "id") Long id, @RequestBody Comment comment) {
        Optional<Comment> updComment = commentService.updateComment(id, comment);
        return updComment.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updComment.get(), HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/blog/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "id") Long id) {
        boolean del = commentService.deleteCommentById(id);
        return del
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
