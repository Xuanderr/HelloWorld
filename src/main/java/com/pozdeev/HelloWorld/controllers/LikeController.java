package com.pozdeev.HelloWorld.controllers;

import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.models.entities.Like;
import com.pozdeev.HelloWorld.models.entities.User;
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

    @PostMapping(path = "/like/put", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putLike(@RequestBody Like newLike) {
        Like like = likeService.addLike(newLike);
        return like.getLikeId() == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/like/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteLike(@RequestBody Like like) {
        boolean del = likeService.removeLike(like);
        return del
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
