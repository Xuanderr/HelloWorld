package com.pozdeev.HelloWorld.repositories;

import com.pozdeev.HelloWorld.models.entities.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends CrudRepository<Comment, Long> {

    List<Comment> findAllByOrderByCommentIdDesc();
}
