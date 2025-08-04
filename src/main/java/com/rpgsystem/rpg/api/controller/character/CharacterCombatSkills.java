package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CombatSkillRequest;
import com.rpgsystem.rpg.api.dto.character.CombatSkillResponse;
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
    public ResponseEntity<List<CombatSkillResponse>> getSkills(@PathVariable String characterId) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.getSkills(characterId, user));
    }

    @GetMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CombatSkillResponse> getSkill(
            @PathVariable String characterId,
            @PathVariable String id) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.getSkill(id, characterId, user));
    }

    @PostMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<CombatSkillResponse> save(
            @PathVariable String characterId,
            @PathVariable String id,
            @Valid @RequestBody CombatSkillRequest request) {
        User user = AuthenticatedUserHelper.get();
        CombatSkillResponse response = service.save(request, id, characterId, user);
        return ResponseEntity.ok(response);
    }
}
