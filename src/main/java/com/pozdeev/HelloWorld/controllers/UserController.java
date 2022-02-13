package com.pozdeev.HelloWorld.controllers;


import com.pozdeev.HelloWorld.models.entities.User;
import com.pozdeev.HelloWorld.models.system_entities.UserProperties;
import com.pozdeev.HelloWorld.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService blogService) {
        this.userService = blogService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.allUsers();
        return users.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getOneUserByID(@PathVariable(name = "id", required = false) Long id) {
        Optional<User> user = userService.oneUser(id);
        return user.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PostMapping(path = "/users" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> create(@RequestBody User newUser) {
        User user = userService.createNewUser(newUser);
        return user.getUserId() == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(user, HttpStatus.OK);
    }

    //authenticated users
    @PutMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> update(@RequestBody User user) {
        User updUser = userService.updateUser(user);
        return updUser == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updUser, HttpStatus.OK);
    }

    @PutMapping(path = "/users/properties", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProperties(@RequestBody UserProperties userProperties) {
        User updUser = userService.updateUserProperties(userProperties);
        return updUser == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(HttpStatus.OK);
    }

    //authenticated users
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id", required = false) Long id) {
        boolean del = userService.deleteUserById(id);
        return del
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
