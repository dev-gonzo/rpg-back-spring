package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.api.dto.character.CharacterHomeDto;
import com.rpgsystem.rpg.application.service.character.CharacterHomeService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterHomeController {

    private final CharacterHomeService service;
    private final AuthenticatedUserProvider userProvider;

    @GetMapping
    @RequireAuthUser
    public ResponseEntity<List<CharacterHomeDto>> listAll() {
        User user = AuthenticatedUserHelper.get();

        return ResponseEntity.ok(service.listByUser(user));
    }

}
