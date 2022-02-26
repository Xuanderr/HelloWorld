package com.pozdeev.HelloWorld.controllers;


import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.models.system_entities.CreateArticleRequest;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/api/v1/blog/posts")
    public ResponseEntity<Page<Article>> getAll(
            @PageableDefault(sort = {"views"}, direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<Article> page = articleService.allArticles(pageable);
        return !page.hasContent()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/api/v1/blog/post/{id}")
    public ResponseEntity<Article> getArticleByID(@PathVariable(name = "id") Long id) {
        Optional<Article> article = articleService.oneArticle(id);
        return article.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(article.get(), HttpStatus.OK);
    }

    @GetMapping("/api/v1/blog/posts/str")
    public ResponseEntity<Page<Article>> getArticlesByStr(
            @PageableDefault(sort = {"views"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "criteria") String criteria)
    {
        Page<Article> page = articleService.articlesByStr(criteria, pageable);
        return !page.hasContent()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/api/v1/blog/posts/tags")
    public ResponseEntity<Page<Article>> getArticlesByTags(HttpServletRequest request,
            @PageableDefault(sort = {"views"}, direction = Sort.Direction.DESC) Pageable pageable)
    {
        String[] tags = request.getParameterValues("tag");
        Page<Article> page = articleService.articlesByTags(pageable, tags);
        return !page.hasContent()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping(path = "/api/v1/blog/post" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> createArticle(@RequestBody CreateArticleRequest request) {
        Optional<Article> article = articleService.createNewArticle(request);
        return article.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(article.get(), HttpStatus.OK);
    }

    @PutMapping(path = "/api/v1/blog/post/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> updateArticle(@PathVariable(name = "id") Long id, @RequestBody Article article) {
        Optional<Article> updArticle = articleService.updateArticle(id, article);
        return updArticle.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updArticle.get(), HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/blog/post/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable(name = "id") Long id) {
        boolean del = articleService.deleteArticleById(id);
        return !del
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}