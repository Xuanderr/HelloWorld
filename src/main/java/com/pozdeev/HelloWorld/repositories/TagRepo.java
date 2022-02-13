package com.pozdeev.HelloWorld.repositories;

import com.pozdeev.HelloWorld.models.entities.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepo extends CrudRepository<Tag, Long> {

    Page<Tag> findAll(Pageable pageable);
}
