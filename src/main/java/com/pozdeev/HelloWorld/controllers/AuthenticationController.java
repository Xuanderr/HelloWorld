package com.pozdeev.HelloWorld.controllers;

import com.pozdeev.HelloWorld.models.entities.Article;
import com.pozdeev.HelloWorld.models.entities.User;
import com.pozdeev.HelloWorld.models.security.AuthenticatedRefreshRequest;
import com.pozdeev.HelloWorld.models.security.AuthenticationRequest;
import com.pozdeev.HelloWorld.models.security.AuthenticationResponse;
import com.pozdeev.HelloWorld.services.ArticleService;
import com.pozdeev.HelloWorld.services.AuthenticationService;
import com.pozdeev.HelloWorld.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    public static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class.getName());

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authRequest) {
        AuthenticationResponse response = authenticationService.login(authRequest);
        return response == null
                ? new ResponseEntity<>(HttpStatus.UNAUTHORIZED)
                : new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> getNewRefreshToken(@RequestBody AuthenticatedRefreshRequest request) {
        AuthenticationResponse response = authenticationService.refresh(request.getRefreshToken());
        return response == null
                ? new ResponseEntity<>(HttpStatus.UNAUTHORIZED)
                : new ResponseEntity<>(response, HttpStatus.OK);
    }




}
