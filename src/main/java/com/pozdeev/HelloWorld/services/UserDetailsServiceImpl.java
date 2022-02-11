package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.User;
import com.pozdeev.HelloWorld.models.security.Status;
import com.pozdeev.HelloWorld.repositories.UserRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class.getName());

    private final UserRepo userRepo;
    private final ApplicationContext applicationContext;

    @Autowired
    public UserDetailsServiceImpl(UserRepo userRepo, ApplicationContext applicationContext) {
        this.userRepo = userRepo;
        this.applicationContext = applicationContext;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.info("IN loadUserByUsername(): Before find user in DB");
        User user  = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", email)));

        AuthenticationService authenticationService =
                (AuthenticationService) applicationContext.getBean("authenticationService");
        authenticationService.setRole(user.getRole());

        LOGGER.info("IN loadUserByUsername(): After success find user in DB");
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getRole().getAuthorities());
    }
}
