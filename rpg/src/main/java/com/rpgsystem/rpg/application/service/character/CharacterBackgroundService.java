package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterBackgroundRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterBackgroundResponse;
import com.rpgsystem.rpg.application.builder.CharacterBackgroundDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterBackgroundUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.BackgroundEntity;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.BackgroundRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterBackgroundService {

    private final BackgroundRepository repository;
    private final CharacterService characterService;
    private final CharacterAccessValidator accessValidator;

    public CharacterBackgroundResponse get(String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        BackgroundEntity entity = repository.findByCharacter_Id(characterId)
                .orElseThrow(() -> new EntityNotFoundException("Background not found for character " + characterId));

        return CharacterBackgroundDtoBuilder.from(entity);
    }

    public CharacterBackgroundResponse save(String characterId, String id, CharacterBackgroundRequest request, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        BackgroundEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));

        new CharacterBackgroundUpdater(request).apply(entity);
        BackgroundEntity saved = repository.save(entity);

        return CharacterBackgroundDtoBuilder.from(saved);
    }
}
