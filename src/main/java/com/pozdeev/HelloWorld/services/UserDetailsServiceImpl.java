package com.pozdeev.HelloWorld.services;

import com.pozdeev.HelloWorld.models.entities.user.User;
import com.pozdeev.HelloWorld.models.security.Status;
import com.pozdeev.HelloWorld.repositories.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepo userRepo;
    private final ApplicationContext applicationContext;

    @Autowired
    public UserDetailsServiceImpl(UserRepo userRepo, ApplicationContext applicationContext) {
        this.userRepo = userRepo;
        this.applicationContext = applicationContext;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user  = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", email)));

        AuthenticationService authenticationService =
                (AuthenticationService) applicationContext.getBean("authenticationService");
        authenticationService.setUser(user);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getStatus().equals(Status.ACTIVE),
                user.getRole().getAuthorities());
    }
}
