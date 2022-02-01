package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.User;
import com.pozdeev.HelloWorld.repositories.ArticleRepo;
import com.pozdeev.HelloWorld.repositories.CommentRepo;
import com.pozdeev.HelloWorld.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    private final ArticleRepo articleRepo;
    private final UserRepo userRepo;
    private final CommentRepo commentRepo;

    @Autowired
    public BlogService(ArticleRepo articleRepo, UserRepo userRepo, CommentRepo commentRepo) {
        this.articleRepo = articleRepo;
        this.userRepo = userRepo;
        this.commentRepo = commentRepo;
    }

    public List<User> findAllUsers() {
        return userRepo.findAllByOrderByUserIdAsc();
    }

    public Optional<User> findUserById(int id) {
        return userRepo.findById(id);
    }

    public User createNewUser(User newUser) {
        return userRepo.save(newUser);
    }

    public User updateUser(User user) {
        Optional<User> updUser = userRepo.findById(user.getUserId());
        if(updUser.isEmpty()) {
            return null;
        }
        updUser.get().setName(user.getName());
        updUser.get().setEmail(user.getEmail());
        updUser.get().setPassword(user.getPassword());
        return userRepo.save(updUser.get());
    }

    public boolean deleteUserById(int id) {
        if (!userRepo.existsById(id)) {
            return false;
        }
        userRepo.deleteById(id);
        return true;
    }
}
