package com.pozdeev.HelloWorld.controllers;


import com.pozdeev.HelloWorld.models.entities.User;
import com.pozdeev.HelloWorld.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/blog")
public class BlogController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BlogController.class.getName());

    private final UserService service;

    @Autowired
    public BlogController(UserService blogService) {
        this.service = blogService;
    }

    @GetMapping("/hello")
    public String forAll() {
        LOGGER.info("IN forAll()");
        return "MAIN BLOG PAGE";
    }

    @GetMapping("/success_login")
    public String successLogin() {
        return "Congratulations! You are authorized.";
    }

    @GetMapping("/auth")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = service.findAllUsers();
        return users.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/auth/{id}")
    public ResponseEntity<User> getOneUser(@PathVariable(name = "id", required = false) Long id) {
        Optional<User> user = service.findUserById(id);
        return user.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PostMapping(path = "/auth" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> registration(@RequestBody User newUser) {
        User user = service.createNewUser(newUser);
        return user.getUserId() == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(user, HttpStatus.OK);
    }

    //@PathVariable(name = "id", required = false) int id,
    @PutMapping(path = "/auth/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updUser = service.updateUser(user);
        return updUser == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updUser, HttpStatus.OK);
    }

    @DeleteMapping("/auth/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id", required = false) Long id) {
        boolean del = service.deleteUserById(id);
        return del
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
