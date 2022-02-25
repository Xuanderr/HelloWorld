package com.pozdeev.HelloWorld.repositories;

import com.pozdeev.HelloWorld.models.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);
}
