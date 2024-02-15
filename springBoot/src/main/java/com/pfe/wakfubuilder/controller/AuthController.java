package com.pfe.wakfubuilder.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pfe.wakfubuilder.model.RegisterRequest;
import com.pfe.wakfubuilder.service.UserService;


@RestController
public class AuthController {

    @Autowired
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        boolean userCreated = userService.createUser(registerRequest.getUsername(), registerRequest.getPassword());

        if (userCreated) {
            return ResponseEntity.ok("User created successfully");
        } else {
            return ResponseEntity.badRequest().body("Username already taken");
        }
    }
}