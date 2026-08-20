package com.Marci.FinVault.API.service;

import com.Marci.FinVault.API.dto.AuthResponse;
import com.Marci.FinVault.API.dto.LoginRequest;
import com.Marci.FinVault.API.entity.User;
import com.Marci.FinVault.API.repository.UserRepository;

public class UserService {

    private UserRepository userRepository;

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
