package com.pozdeev.HelloWorld.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloWorldController {

    @GetMapping("/hello")
    @ResponseBody
    public String sayHello(@RequestParam(name = "name", required = false) String name, Model model) {
        model.addAttribute("message", "Hello " + name);
        return "hello";
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false) String name) {
        System.out.println(name);
        return "greeting";
    }

    @GetMapping("/message")
    @ResponseBody
    public Message generate() {
        return new Message("Hello", "I");
    }

    @GetMapping("/bye")
    public String sayBye() {
        return "bye";
    }
}