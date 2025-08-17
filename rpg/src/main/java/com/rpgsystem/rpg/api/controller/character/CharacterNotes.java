package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CharacterNoteRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterNoteResponse;
import com.rpgsystem.rpg.application.service.character.CharacterNoteService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters/{characterId}/notes")
@RequiredArgsConstructor
public class CharacterNotes {

    private final CharacterNoteService service;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<List<CharacterNoteResponse>> getAll(@PathVariable String characterId) {
        User user = authenticatedUserProvider.getAuthenticatedUser();
        return ResponseEntity.ok(service.getAll(characterId, user));
    }

    @GetMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterNoteResponse> get(
            @PathVariable String characterId,
            @PathVariable String id) {
        User user = authenticatedUserProvider.getAuthenticatedUser();
        return ResponseEntity.ok(service.get(characterId, id, user));
    }

    @PostMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterNoteResponse> save(
            @PathVariable String characterId,
            @PathVariable String id,
            @Valid @RequestBody CharacterNoteRequest request) {
        User user = authenticatedUserProvider.getAuthenticatedUser();
        return ResponseEntity.ok(service.save(characterId, id, request, user));
    }
}
