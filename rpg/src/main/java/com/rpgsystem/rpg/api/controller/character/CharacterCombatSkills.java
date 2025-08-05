package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CharacterCombatSkillRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterCombatSkillResponse;
import com.rpgsystem.rpg.application.service.character.CharacterCombatSkillService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters/{characterId}/combat-skills")
@RequiredArgsConstructor
public class CharacterCombatSkills {

    private final CharacterCombatSkillService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<List<CharacterCombatSkillResponse>> getSkills(@PathVariable String characterId) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.getSkills(characterId, user));
    }

    @GetMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterCombatSkillResponse> getSkill(
            @PathVariable String characterId,
            @PathVariable String id) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.getSkill(id, characterId, user));
    }

    @PostMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CharacterCombatSkillResponse> save(
            @PathVariable String characterId,
            @PathVariable String id,
            @Valid @RequestBody CharacterCombatSkillRequest request) {
        User user = AuthenticatedUserHelper.get();
        CharacterCombatSkillResponse response = service.save(request, id, characterId, user);
        return ResponseEntity.ok(response);
    }
}
