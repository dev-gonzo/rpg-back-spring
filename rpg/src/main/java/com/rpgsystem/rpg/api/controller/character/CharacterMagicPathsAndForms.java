package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsResponse;
import com.rpgsystem.rpg.application.service.character.CharacterPathsAndFormsService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters/{characterId}/magic")
@RequiredArgsConstructor
public class CharacterMagicPathsAndForms {

    private final CharacterPathsAndFormsService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<CharacterPathsAndFormsResponse> get(@PathVariable String characterId) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.get(characterId, user));
    }

    @PostMapping
    @RequireAuthUser
    public ResponseEntity<CharacterPathsAndFormsResponse> save(
            @PathVariable String characterId,
            @Valid @RequestBody CharacterPathsAndFormsRequest request) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.save(characterId, request, user));
    }
}
