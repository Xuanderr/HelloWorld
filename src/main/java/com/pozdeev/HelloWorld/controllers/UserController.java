package com.pozdeev.HelloWorld.controllers;


import com.pozdeev.HelloWorld.models.entities.User;
import com.pozdeev.HelloWorld.repositories.UserRepo;
import com.pozdeev.HelloWorld.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService blogService) {
        this.userService = blogService;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.findAllUsers();
        return users.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getOneUser(@PathVariable(name = "id", required = false) Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PostMapping(path = "/create" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> registration(@RequestBody User newUser) {
        User user = userService.createNewUser(newUser);
        return user.getUserId() == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updUser = userService.updateUser(user);
        return updUser == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updUser, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id", required = false) Long id) {
        boolean del = userService.deleteUserById(id);
        return del
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
