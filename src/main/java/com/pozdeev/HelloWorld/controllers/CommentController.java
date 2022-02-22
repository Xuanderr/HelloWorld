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


@RestController
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Page<Comment>> getAllByArticle(
            @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<Comment> page = commentService.allComments(pageable);
        return page.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping(path = "/comments" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> create(@RequestBody Comment newComment) {
        Comment comment = commentService.createNewComment(newComment);
        return comment.getCommentId() == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PutMapping(path = "/comments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Comment> update(@RequestBody Comment comment) {
        Comment updComment = commentService.updateComment(comment);
        return updComment == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updComment, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        boolean del = commentService.deleteCommentById(id);
        return del
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
