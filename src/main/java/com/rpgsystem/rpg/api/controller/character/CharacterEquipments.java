package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CharacterEquipmentRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterEquipmentResponse;
import com.rpgsystem.rpg.application.service.character.CharacterEquipmentService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters/{characterId}/equipments")
@RequiredArgsConstructor
public class CharacterEquipments {

    private final CharacterEquipmentService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<List<CharacterEquipmentResponse>> getAll(@PathVariable String characterId) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.getAll(characterId, user));
    }

    @GetMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterEquipmentResponse> get(
            @PathVariable String characterId,
            @PathVariable String id) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.get(characterId, id, user));
    }

    @PostMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterEquipmentResponse> save(
            @PathVariable String characterId,
            @PathVariable String id,
            @Valid @RequestBody CharacterEquipmentRequest request) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.save(characterId, id, request, user));
    }
}
