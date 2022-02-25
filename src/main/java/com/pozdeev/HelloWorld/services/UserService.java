package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.user.User;
import com.pozdeev.HelloWorld.models.security.Role;
import com.pozdeev.HelloWorld.models.security.Status;
import com.pozdeev.HelloWorld.models.system_entities.UserProperties;
import com.pozdeev.HelloWorld.repositories.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public Page<User> allUsers(Pageable pageable) throws DataIntegrityViolationException {
        try {
            return userRepo.findAll(pageable);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN UserService.allUsers(): {}", e.getCause(), e);
            throw e;
        }
    }

    public Optional<User> oneUser(Long id) throws DataIntegrityViolationException {
        try {
            return userRepo.findById(id);
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN UserService.oneUser(): {}", e.getCause(), e);
            throw e;
        }
    }

    public Optional<UserProperties> OneUserProperties(String email) throws DataIntegrityViolationException {
        try {
            Optional<User> user = userRepo.findByEmail(email);
            if (user.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(
                    new UserProperties(
                            user.get().getRole().name(), user.get().getStatus().name()));
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN UserService.OneUserProperties(): {}", e.getCause(), e);
            throw e;
        }
    }

    public Optional<User> createNewUser(User newUser) throws DataIntegrityViolationException {
        try {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            newUser.setCreated(LocalDateTime.now());
            return Optional.of(userRepo.save(newUser));
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN UserService.createNewUser(): {}", e.getCause(), e);
            throw e;
        }

    }

    public Optional<User> updateUser(User user) throws DataIntegrityViolationException {
        try {
            Optional<User> updUser = userRepo.findById(user.getUserId());
            if(updUser.isEmpty()) {
                return Optional.empty();
            }
            updUser.get().setName(user.getName());
            updUser.get().setEmail(user.getEmail());
            updUser.get().setPassword(user.getPassword());
            return Optional.of(userRepo.save(updUser.get()));
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN UserService.updateUser(): {}", e.getCause(), e);
            throw e;
        }
    }

    public boolean updateUserProperties(UserProperties user) throws DataIntegrityViolationException {
        try {
            Optional<User> updUser = userRepo.findByEmail(user.getEmail());
            if(updUser.isEmpty()) {
                return false;
            }
            updUser.get().setRole(Role.valueOf(user.getRole()));
            updUser.get().setStatus(Status.valueOf(user.getStatus()));
            userRepo.save(updUser.get());
            return true;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN UserService.updateUserProperties(): {}", e.getCause(), e);
            throw e;
        }
    }

    public boolean deleteUserById(Long id) throws DataIntegrityViolationException {
        try {
            if (!userRepo.existsById(id)) {
                return false;
            }
            userRepo.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            LOGGER.info("IN UserService.deleteUserById(): {}", e.getCause(), e);
            throw e;
        }
    }
}
