package com.pozdeev.HelloWorld.controllers;

import com.pozdeev.HelloWorld.models.entities.Tag;
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

    @GetMapping("/api/v1/blog/tags")
    public ResponseEntity<Page<Tag>> getAll(
            @PageableDefault(sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<Tag> page = tagService.allTags(pageable);
        return page.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(page, HttpStatus.OK);
    }


    @PostMapping(path = "/api/v1/blog/tag" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> createTag(@RequestBody Tag newTag) {
        Optional<Tag> tag = tagService.createNewTag(newTag);
        return tag.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(tag.get(), HttpStatus.OK);
    }

    @PutMapping(path = "/api/v1/blog/tag/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> updateTag(@PathVariable(name = "name") String name, @RequestBody Tag tag) {
        Optional<Tag> updTag = tagService.updateTagByName(name, tag);
        return updTag.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(updTag.get(), HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/blog/tags/{name}")
    public ResponseEntity<?> deleteTag(@PathVariable(name = "name") String name) {
        boolean delTag = tagService.deleteTagByName(name);
        return !delTag
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
