package com.pozdeev.HelloWorld.controllers;


import com.pozdeev.HelloWorld.models.entities.User;
import com.pozdeev.HelloWorld.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/blog")
public class BlogController {

    private final BlogService service;

    @Autowired
    public BlogController(BlogService blogService) {
        this.service = blogService;
    }

    @GetMapping("/hello")
    public String forAll() {
        return "MAIN BLOG PAGE";
    }

    @GetMapping("/success_login")
    public String successLogin() {
        return "Congratulations! You are authorized.";
    }

    @GetMapping("/auth")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = service.findAllUsers();
        return users.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/auth/{id}")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<User> getOneUser(@PathVariable(name = "id", required = false) int id) {
        Optional<User> user = service.findUserById(id);
        return user.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PostMapping(path = "/auth" , consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('users:create')")
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        User user = service.createNewUser(newUser);
        return user.getUserId() == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(user, HttpStatus.OK);
    }

    //@PathVariable(name = "id", required = false) int id,
    @PutMapping(path = "/auth/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('users:update')")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updUser = service.updateUser(user);
        return updUser == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updUser, HttpStatus.OK);
    }

    @DeleteMapping("/auth/{id}")
    @PreAuthorize("hasAuthority('users:delete')")
    public ResponseEntity<?> delete(@PathVariable(name = "id", required = false) int id) {
        boolean del = service.deleteUserById(id);
        return del
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
