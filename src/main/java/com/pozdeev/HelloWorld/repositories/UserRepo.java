package com.pozdeev.HelloWorld.repositories;

import com.pozdeev.HelloWorld.models.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    List<User> findAllByOrderByUserIdAsc();
}
