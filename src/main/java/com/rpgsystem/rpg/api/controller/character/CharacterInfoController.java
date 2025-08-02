package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.application.dto.character.CharacterInfoDto;
import com.rpgsystem.rpg.application.service.character.CharacterInfoService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/characters/{id}/info")
@RequiredArgsConstructor
public class CharacterInfoController {

    private final CharacterInfoService service;
    private final AuthenticatedUserProvider userProvider;


    @GetMapping
    public ResponseEntity<CharacterInfoDto> getInfo(@PathVariable String id) {
        User user = userProvider.getAuthenticatedUser();

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(service.getCharacterById(id));
    }

}
