package com.pozdeev.HelloWorld.controllers;


import com.pozdeev.HelloWorld.Repositories.StateRepo;
import com.pozdeev.HelloWorld.Repositories.StateRepoDatabaseImpl;
import com.pozdeev.HelloWorld.Repositories.StateRepoListImpl;
import com.pozdeev.HelloWorld.models.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class BlogController {

    private StateRepo repo;

    @Autowired
    @Qualifier("database")
    public void setRepo(StateRepo repo) {
        this.repo = repo;
    }

    @GetMapping("/info")
    public String getComponentName() {
        String[] info = Arrays.toString(repo.getClass().getAnnotations()).split("\\.");
        String res = info[info.length-1];
        return res.substring(0, res.length() - 1);
    }

    @GetMapping("/change")
    public void change(@RequestParam(value = "type", required = false) String type) {
        switch (type) {
            case "list" -> setRepo(new StateRepoListImpl());
            case "database" -> setRepo(new StateRepoDatabaseImpl());
        }
    }

    @GetMapping("/states")
    public List<State> read() {
        return repo.readAll();
    }

    @GetMapping("/states/{id}")
    public State read(@PathVariable(name = "id", required = false) int id) {
        State state = repo.read(id);
        if (state == null && getComponentName().endsWith("st\")") ) {
            return new State();
        }
        return state;
    }

    @PostMapping(path = "/states" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public State create(@RequestBody State newState) {
        repo.save(newState);
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
        int upd = repo.update(id, newState);
        if(upd>0) {
            return String.format("State with id = %d updated", id);
        }
        return "Updating failed";
    }

    @DeleteMapping("/states/{id}")
    public String delete(@PathVariable(name = "id", required = false) int id) {
        int del = repo.delete(id);
        if(del>0) {
            return String.format("State with id = %d deleted", id);
        }
        return "Deleting failed";
    }
}