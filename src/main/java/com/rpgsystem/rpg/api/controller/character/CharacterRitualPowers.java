package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerResponse;
import com.rpgsystem.rpg.application.service.character.CharacterRitualPowerService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters/{characterId}/ritual-powers")
@RequiredArgsConstructor
public class CharacterRitualPowers {

    private final CharacterRitualPowerService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<List<CharacterRitualPowerResponse>> getAll(@PathVariable String characterId) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.getAll(characterId, user));
    }

    @GetMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterRitualPowerResponse> get(
            @PathVariable String characterId,
            @PathVariable String id) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.get(characterId, id, user));
    }

    @PostMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterRitualPowerResponse> save(
            @PathVariable String characterId,
            @PathVariable String id,
            @Valid @RequestBody CharacterRitualPowerRequest request) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.save(characterId, id, request, user));
    }
}
