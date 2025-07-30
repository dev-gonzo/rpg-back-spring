package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.application.dto.CharacterHomeDto;
import com.rpgsystem.rpg.application.service.CharacterService;
import com.rpgsystem.rpg.domain.model.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    private final CharacterService service;
    private final AuthenticatedUserProvider userProvider;

    public CharacterController(CharacterService service, AuthenticatedUserProvider userProvider) {
        this.service = service;
        this.userProvider = userProvider;
    }

    @GetMapping
    public ResponseEntity<List<CharacterHomeDto>> listAll() {
        User user = userProvider.getAuthenticatedUser();

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(service.listByUser(user));
    }
}
