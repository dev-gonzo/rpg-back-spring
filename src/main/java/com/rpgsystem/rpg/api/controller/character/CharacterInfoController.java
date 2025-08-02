package com.rpgsystem.rpg.api.controller.character;

import com.rpgsystem.rpg.application.dto.character.CharacterInfoDto;
import com.rpgsystem.rpg.application.dto.character.CharacterInfoRequest;
import com.rpgsystem.rpg.application.service.character.CharacterInfoService;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/characters/{id}/info")
@RequiredArgsConstructor
public class CharacterInfoController {

    private final CharacterInfoService service;
    private final AuthenticatedUserProvider userProvider;

    @GetMapping
    public ResponseEntity<CharacterInfoDto> getInfo(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

//    @PostMapping
//    public ResponseEntity<CharacterInfoDto> createInfo(
//            @RequestBody CharacterInfoRequest request
//    ) {
//        CharacterInfoDto characterInfoDto = service.createInfoDto(request);
//
//        return ResponseEntity.ok(characterInfoDto);
//    }

}
