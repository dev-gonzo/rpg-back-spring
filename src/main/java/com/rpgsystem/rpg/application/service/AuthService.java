package com.rpgsystem.rpg.application.service;

import com.rpgsystem.rpg.application.dto.LoginRequest;
import com.rpgsystem.rpg.application.dto.LoginResponse;
import com.rpgsystem.rpg.domain.model.User;
import com.rpgsystem.rpg.infrastructure.security
        .JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = (User) auth.getPrincipal();
        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }
}
