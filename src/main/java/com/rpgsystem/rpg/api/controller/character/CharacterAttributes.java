package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CharacterAttributeModRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterAttributeRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterAttributeResponse;
import com.rpgsystem.rpg.application.service.character.CharacterAttributesService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters/{characterId}/attributes")
@RequiredArgsConstructor
public class CharacterAttributes {

    private final CharacterAttributesService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<CharacterAttributeResponse> getAttributes(
            @PathVariable String characterId) {

        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.getCharacterAttributes(characterId, user));
    }

    @PostMapping
    @RequireAuthUser
    public ResponseEntity<CharacterAttributeResponse> save(
            @PathVariable String characterId,
            @Valid @RequestBody CharacterAttributeRequest request
    ) {

        User user = AuthenticatedUserHelper.get();
        CharacterAttributeResponse characterAttributeResponse = service.save(request, characterId, user);

        return ResponseEntity.ok(characterAttributeResponse);
    }

    @PostMapping("/mod")
    @RequireAuthUser
    public ResponseEntity<CharacterAttributeResponse> saveMod(
            @PathVariable String characterId,
            @Valid @RequestBody CharacterAttributeModRequest request
    ) {
        
        User user = AuthenticatedUserHelper.get();
        CharacterAttributeResponse characterAttributeResponse = service.saveMod(request, characterId, user);

        return ResponseEntity.ok(characterAttributeResponse);
    }
}
