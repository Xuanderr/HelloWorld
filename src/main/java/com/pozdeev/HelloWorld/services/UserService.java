package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.User;
import com.pozdeev.HelloWorld.repositories.ArticleRepo;
import com.pozdeev.HelloWorld.repositories.CommentRepo;
import com.pozdeev.HelloWorld.repositories.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class.getName());

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder =passwordEncoder;
    }

    public List<User> findAllUsers() {
        LOGGER.info("IN findAllUsers()");
        return userRepo.findAllByOrderByUserIdAsc();
    }

    public Optional<User> getByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public Optional<User> findUserById(Long id) {
        LOGGER.info("IN findUserById()");
        return userRepo.findById(id);
    }

    public User createNewUser(User newUser) {
        LOGGER.info("IN createNewUser()");
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setCreated(LocalDateTime.now());
        return userRepo.save(newUser);
    }

    public User updateUser(User user) {
        LOGGER.info("IN updateUser(): Before find user in DB");
        Optional<User> updUser = userRepo.findById(user.getUserId());
        if(updUser.isEmpty()) {
            return null;
        }
        LOGGER.info("IN updateUser(): After success find user in DB");
        updUser.get().setName(user.getName());
        updUser.get().setEmail(user.getEmail());
        updUser.get().setPassword(user.getPassword());
        return userRepo.save(updUser.get());
    }


    public boolean deleteUserById(Long id) {
        LOGGER.info("IN deleteUserById(): Before find user in DB");
        if (!userRepo.existsById(id)) {
            return false;
        }
        LOGGER.info("IN deleteUserById(): After success find user in DB");
        userRepo.deleteById(id);
        return true;
    }
}
