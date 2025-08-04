package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleResponse;
import com.rpgsystem.rpg.application.service.character.CharacterRelevantPeopleService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters/{characterId}/relevant-people")
@RequiredArgsConstructor
public class CharacterRelevantPeople {

    private final CharacterRelevantPeopleService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<List<CharacterRelevantPeopleResponse>> getAll(@PathVariable String characterId) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.getAll(characterId, user));
    }

    @GetMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterRelevantPeopleResponse> get(
            @PathVariable String characterId,
            @PathVariable String id) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.get(characterId, id, user));
    }

    @PostMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterRelevantPeopleResponse> save(
            @PathVariable String characterId,
            @PathVariable String id,
            @Valid @RequestBody CharacterRelevantPeopleRequest request) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.save(characterId, id, request, user));
    }
}
