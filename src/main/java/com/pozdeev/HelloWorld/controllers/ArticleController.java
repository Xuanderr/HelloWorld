package com.pozdeev.HelloWorld.controllers;


import com.pozdeev.HelloWorld.models.entities.Article;
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

@RestController
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/posts")
    public ResponseEntity<Page<Article>> getAll(
            @PageableDefault(sort = {"views"}, direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<Article> page = articleService.allArticles(pageable);
        return page.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Article> getOneArticleByID(@PathVariable(name = "id") Long id) {
        Article article = articleService.oneArticle(id);
        return article == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(article, HttpStatus.OK);
    }

    @GetMapping("/posts/str")
    public ResponseEntity<Page<Article>> getArticleByStr(
            @PageableDefault(sort = {"views"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "criteria") String criteria)
    {
        Page<Article> page = articleService.oneArticleByStr(criteria, pageable);
        return page.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/posts/likes/{id}")
    public ResponseEntity<?> likes(@PathVariable(name = "id") Long id, @RequestParam(name = "likes") int likes) {
        Article article = articleService.saveCurrentLikes(id, likes);
        return article == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/posts" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> create(@RequestBody Article newArticle) {
        Article article = articleService.createNewArticle(newArticle);
        return article.getArticleId() == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PutMapping(path = "/posts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> update(@RequestBody Article article) {
        Article updArticle = articleService.updateArticle(article);
        return updArticle == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updArticle, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        boolean del = articleService.deleteArticleById(id);
        return del
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}