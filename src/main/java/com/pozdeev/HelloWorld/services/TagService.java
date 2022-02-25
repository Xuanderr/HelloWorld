package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.Tag;
import com.pozdeev.HelloWorld.repositories.TagRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TagService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TagService.class.getName());
    private final TagRepo tagRepo;

    @Autowired
    public TagService(TagRepo tagRepo) {
        this.tagRepo = tagRepo;
    }

    public Page<Tag> allTags(Pageable pageable) throws DataIntegrityViolationException {
        try {
            return tagRepo.findAll(pageable);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN TagService.allTags(): {}", e.getCause(), e);
            throw e;
        }
    }

    public Optional<Tag> createNewTag(Tag newTag) throws DataIntegrityViolationException {
        try {
            return Optional.of(tagRepo.save(newTag));
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN TagService.createNewTag(): {}", e.getCause(), e);
            throw e;
        }
    }

    public Optional<Tag> updateTagByName(String name, Tag tag) throws DataIntegrityViolationException {
        try {
            Optional<Tag> updTag = tagRepo.findByName(name);
            if(updTag.isEmpty()) {
                return Optional.empty();
            }
            updTag.get().setName(tag.getName());
            return Optional.of(tagRepo.save(updTag.get()));
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN TagService.updateTagByName(): {}", e.getCause(), e);
            throw e;
        }
    }

    public boolean deleteTagByName(String name) throws DataIntegrityViolationException {
        try {
            if (!tagRepo.existsByName(name)) {
                return false;
            }
            tagRepo.deleteByName(name);
            return true;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN TagService.deleteTagByName(): {}", e.getCause(), e);
            throw e;
        }
    }

}
