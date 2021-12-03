package com.pozdeev.HelloWorld.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public String greeting() {
        return "Hello_World";
    }

    @GetMapping("/message")
    public Message generate() {
        return new Message("Hello", "I");
    }
}