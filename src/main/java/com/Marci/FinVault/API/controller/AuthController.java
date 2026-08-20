package com.Marci.FinVault.API.controller;

import com.Marci.FinVault.API.dto.AuthResponse;
import com.Marci.FinVault.API.dto.LoginRequest;
import com.Marci.FinVault.API.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public AuthResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null  || loginRequest.getPassword() == null) {
            throw new IllegalArgumentException("Email and password must not be null");
        } else if (loginRequest.getEmail().isEmpty() || loginRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Email and password must not be empty");
        }else if (loginRequest.getEmail().length() < 8 || loginRequest.getPassword().length() < 8) {
            throw new IllegalArgumentException("Email and password must be at least 8 characters long");
        }
        return authService.authenticateUser(loginRequest);
    }
}
