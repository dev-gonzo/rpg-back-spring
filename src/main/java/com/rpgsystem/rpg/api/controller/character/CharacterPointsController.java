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
@RequestMapping("/characters/{id}/points")
@RequiredArgsConstructor
public class CharacterPointsController {

    private final CharacterPointService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<CharacterPointsResponse> getInfo(@PathVariable String id) {
        return ResponseEntity.ok(service.getInfo(id));
    }

    @PostMapping
    @RequireAuthUser
    public ResponseEntity<CharacterPointsResponse> save(
            @PathVariable String id,
           @Valid @RequestBody CharacterPointsRequest request
    ) {
        User user = AuthenticatedUserHelper.get();
        CharacterPointsResponse characterInfoResponse = service.save(request, id, user);

        return ResponseEntity.ok(characterInfoResponse);
    }

    @PostMapping("/current")
    @RequireAuthUser
    public ResponseEntity<CharacterPointsResponse> saveCurrent(
            @PathVariable String id,
           @Valid @RequestBody CharacterCurrentPointsRequest request
    ) {
        User user = AuthenticatedUserHelper.get();
        CharacterPointsResponse characterInfoResponse = service.saveCurrentPatch(request, id, user);

        return ResponseEntity.ok(characterInfoResponse);
    }

    @PostMapping("/current/patch")
    @RequireAuthUser
    public ResponseEntity<CharacterPointsResponse> saveCurrentPatch(
            @PathVariable String id,
           @Valid @RequestBody CharacterCurrentPointsRequest request
    ) {
        User user = AuthenticatedUserHelper.get();
        CharacterPointsResponse characterInfoResponse = service.saveCurrentPatch(request, id, user);

        return ResponseEntity.ok(characterInfoResponse);
    }

}
