package com.Marci.FinVault.API.service;

import com.Marci.FinVault.API.dto.AuthResponse;
import com.Marci.FinVault.API.dto.LoginRequest;
import com.Marci.FinVault.API.entity.User;
import com.Marci.FinVault.API.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class AuthService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        if (user.isEmpty() || !passwordEncoder.matches(
                loginRequest.getPassword(),
                user.get().getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return new AuthResponse(jwtToken);    }

}
