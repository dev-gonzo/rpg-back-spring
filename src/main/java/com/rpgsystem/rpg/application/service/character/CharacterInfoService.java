package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.application.builder.CharacterInfoDtoBuilder;
import com.rpgsystem.rpg.application.dto.character.CharacterInfoDto;
import com.rpgsystem.rpg.domain.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterInfoService {

    private final CharacterRepository repository;

    public CharacterInfoDto getCharacterById(String id) {

       return repository.findById(id).map(CharacterInfoDtoBuilder::from).orElse(null);

    }
}
