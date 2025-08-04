package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.SkillRequest;
import com.rpgsystem.rpg.api.dto.character.SkillResponse;
import com.rpgsystem.rpg.application.service.character.CharacterSkillService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/characters/{characterId}/skills")
@RequiredArgsConstructor
public class CharacterSkills {

    private final CharacterSkillService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<List<SkillResponse>> getSkills(@PathVariable String characterId) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.getSkills(characterId, user));
    }

    @GetMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<SkillResponse> getSkill(
            @PathVariable String characterId,
            @PathVariable String id) {

        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(service.getSkill(id, characterId, user));
    }

    @PostMapping("/{id}")
    @RequireAuthUser
    public ResponseEntity<SkillResponse> save(
            @PathVariable String characterId,
            @PathVariable String id,
            @Valid @RequestBody SkillRequest request
    ) {
        User user = AuthenticatedUserHelper.get();
        SkillResponse response = service.save(request, id, characterId, user);
        return ResponseEntity.ok(response);
    }
}
