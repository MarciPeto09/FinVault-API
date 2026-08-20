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


}
