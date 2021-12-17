package com.pozdeev.HelloWorld.controllers;


import com.pozdeev.HelloWorld.services.StateService;
import com.pozdeev.HelloWorld.models.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// IF NOT STARTED GO TO StateServiceDatabaseImpl.java LINE 10

@RestController
public class BlogController {

    private final StateService repository;

    @Autowired
    public BlogController(StateService repository) {
        this.repository = repository;
    }

    @GetMapping("/states")
    public List<State> read() {
        return repository.readAll();
    }

    @GetMapping("/states/{id}")
    public State read(@PathVariable(name = "id", required = false) int id) {
        State state = repository.read(id);
        if (state == null) {
            return new State();
        }
        return state;
    }

    @PostMapping(path = "/states" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public State create(@RequestBody State newState) {
        repository.save(newState);
        return newState;
    }

//    Усложненый вариант @PostMapping без аннотации @ModelAttribute
//    @PostMapping()
//    public String create(@RequestParam("name") String name, @RequestParam("surname") String surname,
//                         @RequestParam("email") String email, Model model) {
//
//        Person person = new Person(); //автоматически делается при аннотации @ModelAttribute
//        person.setName(name); //автоматически делается при аннотации @ModelAttribute
//
//        //добавление в БД
//
//        model.addAttribute("person", person); //автоматически делается при аннотации @ModelAttribute
//        return "successPage";
//    }
//
//
//    @PostMapping()
//    public String create(@ModelAttribute("person") Person person) {
//        personDAO.save(person);
//        return "redirect:/people";
//    }

    @PutMapping(path = "/states/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String update(@PathVariable(name = "id", required = false) int id, @RequestBody State newState) {
        boolean upd = repository.update(id, newState);
        if(upd) {
            return String.format("State with id = %d updated", id);
        }
        return "Updating failed";
    }

    @DeleteMapping("/states/{id}")
    public String update(@PathVariable(name = "id", required = false) int id) {
        boolean del = repository.delete(id);
        if(del) {
            return String.format("State with id = %d deleted", id);
        }
        return "Deleting failed";
    }
}