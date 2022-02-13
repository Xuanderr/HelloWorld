package com.pozdeev.HelloWorld.controllers;

import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.models.entities.Tag;
import com.pozdeev.HelloWorld.models.entities.User;
import com.pozdeev.HelloWorld.models.system_entities.UserProperties;
import com.pozdeev.HelloWorld.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    //permit all
    @GetMapping("/tags")
    public ResponseEntity<Page<Tag>> getAll(
            @PageableDefault(sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<Tag> page = tagService.allTags(pageable);
        return page.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(page, HttpStatus.OK);
    }


    //admin
    @PostMapping(path = "/tags" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> create(@RequestBody Tag newTag) {
        Tag tag = tagService.createNewTag(newTag);
        return tag.getId() == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(tag, HttpStatus.OK);
    }

    //admin
    @PutMapping(path = "/tags", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> update(@RequestBody Tag tag) {
        Tag updTag = tagService.updateTag(tag);
        return updTag == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updTag, HttpStatus.OK);
    }

    //admin
    @DeleteMapping("/tags/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        boolean del = tagService.deleteTagById(id);
        return del
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
