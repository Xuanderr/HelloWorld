package com.pozdeev.HelloWorld.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloSimpleController {

    @GetMapping("/hello")
    public String hello() {
        return "MAIN BLOG PAGE";
    }

}
