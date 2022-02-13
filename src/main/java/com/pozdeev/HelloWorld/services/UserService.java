package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.User;
import com.pozdeev.HelloWorld.models.security.Role;
import com.pozdeev.HelloWorld.models.security.Status;
import com.pozdeev.HelloWorld.models.system_entities.UserProperties;
import com.pozdeev.HelloWorld.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder =passwordEncoder;
    }

    public List<User> allUsers() {
        return userRepo.findAllByOrderByUserIdAsc();
    }

    //сейчас не используется
    public Optional<User> getByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public Optional<User> oneUser(Long id) {
        return userRepo.findById(id);
    }

    public User createNewUser(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setCreated(LocalDateTime.now());
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

    public User updateUserProperties(UserProperties userProperties) {
        Optional<User> updUser = userRepo.findByEmail(userProperties.getEmail());
        if(updUser.isEmpty()) {
            return null;
        }
        updUser.get().setRole(Role.valueOf(userProperties.getRole()));
        updUser.get().setStatus(Status.valueOf(userProperties.getStatus()));
        return userRepo.save(updUser.get());
    }

    public boolean deleteUserById(Long id) {
        if (!userRepo.existsById(id)) {
            return false;
        }
        userRepo.deleteById(id);
        return true;
    }
}
