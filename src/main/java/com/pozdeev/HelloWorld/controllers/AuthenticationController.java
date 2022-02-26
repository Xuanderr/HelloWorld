package com.pozdeev.HelloWorld.controllers;

import com.pozdeev.HelloWorld.models.system_entities.AuthenticatedRefreshRequest;
import com.pozdeev.HelloWorld.models.system_entities.AuthenticationRequest;
import com.pozdeev.HelloWorld.models.system_entities.AuthenticationResponse;
import com.pozdeev.HelloWorld.models.system_entities.LoginResponse;
import com.pozdeev.HelloWorld.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/api/v1/blog/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthenticationRequest authRequest) {
        LoginResponse response = new LoginResponse();
        boolean result = authenticationService.login(authRequest, response);
        return !result
                ? new ResponseEntity<>(HttpStatus.UNAUTHORIZED)
                : new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/api/v1/blog/refresh")
    public ResponseEntity<AuthenticationResponse> getNewTokens(@RequestBody AuthenticatedRefreshRequest request) {
        AuthenticationResponse response = authenticationService.refresh(request.getRefreshToken());
        return response == null
                ? new ResponseEntity<>(HttpStatus.UNAUTHORIZED)
                : new ResponseEntity<>(response, HttpStatus.OK);
    }

}
