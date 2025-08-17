package com.rpgsystem.rpg.application.service;

import com.rpgsystem.rpg.api.dto.LoginRequest;
import com.rpgsystem.rpg.api.dto.LoginResponse;
import com.rpgsystem.rpg.api.dto.RegisterRequest;
import com.rpgsystem.rpg.api.dto.RegisterResponse;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.exception.InvalidCredentialsException;
import com.rpgsystem.rpg.domain.repository.UserRepository;
import com.rpgsystem.rpg.infrastructure.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, 
                      UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            User user = (User) auth.getPrincipal();
            String token = jwtService.generateToken(user);
            return new LoginResponse(token);

        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException();
        }
    }

    public RegisterResponse register(RegisterRequest request) {
        // Verificar se o email já existe
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Criar novo usuário
        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .country(request.getCountry())
                .city(request.getCity())
                .isMaster(false)
                .build();

        User savedUser = userRepository.save(user);

        return RegisterResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .message("User registered successfully")
                .build();
    }

}
