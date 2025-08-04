package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CharacterCurrentPointsRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterPointsRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterPointsResponse;
import com.rpgsystem.rpg.application.service.character.CharacterPointService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters/{characterId}/points")
@RequiredArgsConstructor
public class CharacterPointsController {

    private final CharacterPointService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<CharacterPointsResponse> getInfo(
            @PathVariable String characterId) {

        return ResponseEntity.ok(service.getInfo(characterId));
    }

    @PostMapping
    @RequireAuthUser
    public ResponseEntity<CharacterPointsResponse> save(
            @PathVariable String characterId,
            @Valid @RequestBody CharacterPointsRequest request
    ) {

        User user = AuthenticatedUserHelper.get();
        CharacterPointsResponse characterInfoResponse = service.save(request, characterId, user);

        return ResponseEntity.ok(characterInfoResponse);
    }

    @PostMapping("/current")
    @RequireAuthUser
    public ResponseEntity<CharacterPointsResponse> saveCurrent(
            @PathVariable String characterId,
            @Valid @RequestBody CharacterCurrentPointsRequest request
    ) {

        User user = AuthenticatedUserHelper.get();
        CharacterPointsResponse characterInfoResponse = service.saveCurrentAdjust(request, characterId, user);

        return ResponseEntity.ok(characterInfoResponse);
    }

    @PostMapping("/current/adjust")
    @RequireAuthUser
    public ResponseEntity<CharacterPointsResponse> saveCurrentAdjust(
            @PathVariable String characterId,
            @Valid @RequestBody CharacterCurrentPointsRequest request
    ) {

        User user = AuthenticatedUserHelper.get();
        CharacterPointsResponse characterInfoResponse = service.saveCurrentAdjust(request, characterId, user);

        return ResponseEntity.ok(characterInfoResponse);
    }

}
