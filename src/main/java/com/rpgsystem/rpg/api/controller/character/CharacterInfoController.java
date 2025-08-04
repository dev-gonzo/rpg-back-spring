package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CharacterInfoRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.application.service.character.CharacterInfoService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters/{characterId}/info")
@RequiredArgsConstructor
public class CharacterInfoController {

    private final CharacterInfoService service;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<CharacterInfoResponse> getInfo(
            @PathVariable String characterId) {

        return ResponseEntity.ok(service.getInfo(characterId));
    }

    @PostMapping
    @RequireAuthUser
    public ResponseEntity<CharacterInfoResponse> save(
            @PathVariable String characterId,
            @Valid @RequestBody CharacterInfoRequest request
    ) {

        User user = AuthenticatedUserHelper.get();
        CharacterInfoResponse characterInfoResponse = service.save(request, characterId, user);

        return ResponseEntity.ok(characterInfoResponse);
    }

}
