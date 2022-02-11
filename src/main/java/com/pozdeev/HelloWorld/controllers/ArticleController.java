package com.pozdeev.HelloWorld.controllers;


import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.repositories.ArticleRepo;
import com.pozdeev.HelloWorld.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/main")
    public ResponseEntity<Page<Article>> getMainPage(
            @PageableDefault(sort = {"views"}, direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<Article> page = articleService.getAll(pageable);
        return page.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(page, HttpStatus.OK);
    }

    //like

//    @GetMapping("/main")
//    public ResponseEntity<Page<Article>> getMainPage(
//            @PageableDefault(sort = {"views"}, direction = Sort.Direction.DESC) Pageable pageable)
//    {
//        Page<Article> page = articleService.getAll(pageable);
//        return page.isEmpty()
//                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
//                : new ResponseEntity<>(page, HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Article> read(@PathVariable(name = "id", required = false) int id) {
//
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
//
//    @PostMapping(path = "/" , consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> create(@RequestBody Article newArticle) {
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

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


//    @PostMapping()
//    public String create(@ModelAttribute("person") Person person) {
//        personDAO.save(person);
//        return "redirect:/people";
//    }

//    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?>  update(@PathVariable(name = "id", required = false) int id, @RequestBody Article newArticle) {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> delete(@PathVariable(name = "id", required = false) int id) {
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}