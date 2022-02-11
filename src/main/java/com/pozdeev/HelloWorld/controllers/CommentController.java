package com.pozdeev.HelloWorld.controllers;

import com.pozdeev.HelloWorld.models.entities.Comment;
import com.pozdeev.HelloWorld.repositories.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("comments")
public class CommentController {

    private CommentRepo repo;

    @Autowired
    public void setRepo(CommentRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public ResponseEntity<List<Comment>> read() {
        List<Comment> comments = repo.findAllByOrderByCommentIdDesc();
        return comments.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> read(@PathVariable(name = "id", required = false) Long id) {
        Optional<Comment> comment = repo.findById(id);
        return comment.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(comment.get(), HttpStatus.OK);
    }

    @PostMapping(path = "/" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody Comment newUSer) {
        Comment comment = repo.save(newUSer);
        return comment.getCommentId() != null
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  update(@PathVariable(name = "id", required = false) Long id, @RequestBody Comment newComment) {
        if (!repo.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        Comment user = new Comment(id, newComment.getArticle(), newComment.getAuthor(), newComment.getFullText());
//        repo.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id", required = false) Long id) {
        if (!repo.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
