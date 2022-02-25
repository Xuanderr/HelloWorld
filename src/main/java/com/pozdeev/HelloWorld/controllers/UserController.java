package com.pozdeev.HelloWorld.controllers;


import com.pozdeev.HelloWorld.models.entities.user.User;
import com.pozdeev.HelloWorld.models.system_entities.UserProperties;
import com.pozdeev.HelloWorld.services.UserService;
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
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService blogService) {
        this.userService = blogService;
    }

    @GetMapping("/api/v1/blog/users")
    public ResponseEntity<Page<User>> getAll(
            @PageableDefault(sort = {"name"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<User> users = userService.allUsers(pageable);
        return !users.hasContent()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/api/v1/blog/user/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable(name = "id", required = false) Long id) {
        Optional<User> user = userService.oneUser(id);
        return user.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PostMapping(path = "/api/v1/blog/user" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        Optional<User> user = userService.createNewUser(newUser);
        return user.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PutMapping(path = "/api/v1/blog/user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable(name = "id") Long id, @RequestBody User user) {
        user.setUserId(id);
        Optional<User> updUser = userService.updateUser(user);
        return updUser.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updUser.get(), HttpStatus.OK);
    }

    @PutMapping(path = "/api/v1/blog/user/properties", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserProperties(@RequestBody UserProperties user) {
        boolean updUserProperties = userService.updateUserProperties(user);
        return !updUserProperties
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/blog/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") Long id) {
        boolean delUser = userService.deleteUserById(id);
        return delUser
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
