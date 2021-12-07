package com.pozdeev.HelloWorld.controllers;


import com.pozdeev.HelloWorld.dao.PersonDAO;
import com.pozdeev.HelloWorld.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/people")
public class PeopleController {


    private PersonDAO personDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }
    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        //model.addAttribute("person", new Person());
        return "people/new";
    }


//    //Усложненый вариант @PostMapping без аннотации @ModelAttribute
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


    @PostMapping()
    public String create(@ModelAttribute("person") Person person) {
        personDAO.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.show(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
        personDAO.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personDAO.delete(id);
        return "redirect:/people";
    }
}
