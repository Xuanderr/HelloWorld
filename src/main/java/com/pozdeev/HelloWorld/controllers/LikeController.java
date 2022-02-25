package com.pozdeev.HelloWorld.controllers;

import com.pozdeev.HelloWorld.models.entities.Like;
import com.pozdeev.HelloWorld.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping(path = "/api/v1/blog/like/put", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putLike(@RequestBody Like newLike) {
        boolean addLike = likeService.putLike(newLike);
        return !addLike
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/api/v1/blog/like/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteLike(@RequestBody Like like) {
        boolean delLike = likeService.removeLike(like);
        return !delLike
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
