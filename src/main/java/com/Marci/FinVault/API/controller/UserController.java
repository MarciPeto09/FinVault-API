package com.Marci.FinVault.API.controller;

import com.Marci.FinVault.API.dto.AuthResponse;
import com.Marci.FinVault.API.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.Marci.FinVault.API.service.UserService;

@Controller
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private  UserService userService;


    @GetMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null  || loginRequest.getPassword() == null) {
            throw new IllegalArgumentException("Email and password must not be null");
        } else if (loginRequest.getEmail().isEmpty() || loginRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Email and password must not be empty");
        }else if (loginRequest.getEmail().length() < 8 || loginRequest.getPassword().length() < 8) {
            throw new IllegalArgumentException("Email and password must be at least 8 characters long");
        }
        return userService.login(loginRequest);
    }

}
