package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CharacterBackgroundRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterBackgroundResponse;
import com.rpgsystem.rpg.application.service.character.CharacterBackgroundService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters/{characterId}/background")
@RequiredArgsConstructor
public class CharacterBackgroundController {

    private final CharacterBackgroundService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<CharacterBackgroundResponse> get(@PathVariable String characterId) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.get(characterId, user));
    }

    @PostMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterBackgroundResponse> save(
            @PathVariable String characterId,
            @PathVariable String id,
            @Valid @RequestBody CharacterBackgroundRequest request
    ) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.save(characterId, id, request, user));
    }
}
