package com.pozdeev.HelloWorld.repositories;

import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.models.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends CrudRepository<Comment, Long> {

    Page<Comment> findAll(Pageable pageable);

    List<Comment> findAllByOrderByCommentIdDesc();
}
