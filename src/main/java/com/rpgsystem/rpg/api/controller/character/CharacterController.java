package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.api.dto.character.CharacterInfoRequest;
import com.rpgsystem.rpg.application.service.character.CharacterService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final CharacterService characterService;


    @PostMapping
    @RequireAuthUser
    public ResponseEntity<CharacterInfoResponse> create(
            @Valid @RequestBody CharacterInfoRequest request
    ) {
        User user = AuthenticatedUserHelper.get();
        return ResponseEntity.ok(characterService.create(request, user));
    }

}
