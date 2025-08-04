package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.*;
import com.rpgsystem.rpg.application.service.character.CharacterImprovementService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters/{characterId}/improvements")
@RequiredArgsConstructor
public class CharacterImprovements {

    private final CharacterImprovementService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<List<CharacterImprovementResponse>> getImprovements(
            @PathVariable String characterId) {

        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.getImprovements(characterId, user));
    }

    @GetMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterImprovementResponse> getImprovement(
            @PathVariable String characterId,
            @PathVariable String id) {

        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.getImprovement(id, characterId, user));
    }

    @PostMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterImprovementResponse> save(
            @PathVariable String characterId,
            @PathVariable String id,
            @Valid @RequestBody CharacterImprovementRequest request
    ) {

        User user = AuthenticatedUserHelper.get();
        CharacterImprovementResponse characterAttributeResponse = service.save(request, id, characterId, user);

        return ResponseEntity.ok(characterAttributeResponse);
    }


}
