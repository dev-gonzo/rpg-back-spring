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
@RequestMapping("/characters/{id}/attributes")
@RequiredArgsConstructor
public class CharacterAttributes {

    private final CharacterAttributesService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<CharacterAttributeResponse> getInfo(@PathVariable String id) {
        return ResponseEntity.ok(service.getCharacterAttributes(id));
    }

    @PostMapping
    @RequireAuthUser
    public ResponseEntity<CharacterAttributeResponse> save(
            @PathVariable String id,
            @Valid @RequestBody CharacterAttributeRequest request
    ) {
        User user = AuthenticatedUserHelper.get();
        CharacterAttributeResponse characterAttributeResponse = service.save(request, id, user);

        return ResponseEntity.ok(characterAttributeResponse);
    }

    @PostMapping("/mod")
    @RequireAuthUser
    public ResponseEntity<CharacterAttributeResponse> saveMod(
            @PathVariable String id,
            @Valid @RequestBody CharacterAttributeModRequest request
    ) {
        User user = AuthenticatedUserHelper.get();
        CharacterAttributeResponse characterAttributeResponse = service.saveMod(request, id, user);

        return ResponseEntity.ok(characterAttributeResponse);
    }
}
