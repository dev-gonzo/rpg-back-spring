package com.rpgsystem.rpg.api.controller.auth.login;

import com.rpgsystem.rpg.application.dto.LoginRequest;
import com.rpgsystem.rpg.application.dto.LoginResponse;
import com.rpgsystem.rpg.application.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
}
