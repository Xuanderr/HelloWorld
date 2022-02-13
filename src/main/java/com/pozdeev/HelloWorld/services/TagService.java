package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.Tag;
import com.pozdeev.HelloWorld.repositories.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagService {

    private final TagRepo tagRepo;

    @Autowired
    public TagService(TagRepo tagRepo) {
        this.tagRepo = tagRepo;
    }

    public Page<Tag> allTags(Pageable pageable) {
        return tagRepo.findAll(pageable);
    }

    public Tag createNewTag(Tag newTag) {
        return tagRepo.save(newTag);
    }

    public Tag updateTag(Tag tag) {
        Optional<Tag> updTag = tagRepo.findById(tag.getId());
        if(updTag.isEmpty()) {
            return null;
        }
        updTag.get().setName(tag.getName());
        return tagRepo.save(updTag.get());
    }

    public boolean deleteTagById(Long id) {
        if (!tagRepo.existsById(id)) {
            return false;
        }
        tagRepo.deleteById(id);
        return true;
    }

}
