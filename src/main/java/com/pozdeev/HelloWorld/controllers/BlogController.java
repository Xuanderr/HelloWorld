package com.pozdeev.HelloWorld.controllers;


import com.pozdeev.HelloWorld.services.StateService;
import com.pozdeev.HelloWorld.models.State;
import com.pozdeev.HelloWorld.services.StateServiceDatabaseImpl;
import com.pozdeev.HelloWorld.services.StateServiceListImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class BlogController {

    private StateService service;

    @Autowired
    @Qualifier("list")
    public void setService(StateService service) {
        this.service = service;
    }

    @GetMapping("/info")
    public String getComponentName() {
        String[] info = Arrays.toString(service.getClass().getAnnotations()).split("\\.");
        String res = info[info.length-1];
        return res.substring(0, res.length() - 1);
    }

    @GetMapping("/change")
    public void change(@RequestParam(value = "type", required = false) String type) {
        switch (type) {
            case "list" -> setService(new StateServiceListImpl());
            case "database" -> setService(new StateServiceDatabaseImpl());
        }
    }

    @GetMapping("/states")
    public List<State> read() {
        return service.readAll();
    }

    @GetMapping("/states/{id}")
    public State read(@PathVariable(name = "id", required = false) int id) {
        State state = service.read(id);
        if (state == null && getComponentName().endsWith("st\")") ) {
            return new State();
        }
        return state;
    }

    @PostMapping(path = "/states" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public State create(@RequestBody State newState) {
        service.save(newState);
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
        int upd = service.update(id, newState);
        if(upd>0) {
            return String.format("State with id = %d updated", id);
        }
        return "Updating failed";
    }

    @DeleteMapping("/states/{id}")
    public String delete(@PathVariable(name = "id", required = false) int id) {
        int del = service.delete(id);
        if(del>0) {
            return String.format("State with id = %d deleted", id);
        }
        return "Deleting failed";
    }
}