package com.pfe.wakfubuilder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pfe.wakfubuilder.model.RegisterRequest;
import com.pfe.wakfubuilder.model.User;
import com.pfe.wakfubuilder.service.UserService;


@RestController
public class AuthController {

    @Autowired
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest registerRequest) {
        User userCreated = userService.createUser(registerRequest.getUsername(), registerRequest.getPassword());
        return userCreated;  
    }

    @PostMapping("/login")
    public User login(@RequestBody RegisterRequest registerRequest) {
        User user = userService.getUserLogin(registerRequest.getUsername(), registerRequest.getPassword());
        return user; 
    }
}
