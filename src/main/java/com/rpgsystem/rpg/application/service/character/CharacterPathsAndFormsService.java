package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsResponse;
import com.rpgsystem.rpg.application.builder.CharacterPathsAndFormsDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterPathsAndFormsUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.PathsAndFormsEntity;
import com.rpgsystem.rpg.domain.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterPathsAndFormsService {

    private final com.rpgsystem.rpg.domain.repository.character.PathsAndFormsRepository repository;
    private final CharacterService characterService;
    private final CharacterAccessValidator accessValidator;

    public CharacterPathsAndFormsResponse get(String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        PathsAndFormsEntity entity = repository.findById(characterId)
                .orElseThrow(() -> new EntityNotFoundException(characterId));

        return CharacterPathsAndFormsDtoBuilder.from(entity);
    }

    public CharacterPathsAndFormsResponse save(String characterId, CharacterPathsAndFormsRequest request, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        PathsAndFormsEntity entity = repository.findById(characterId)
                .orElseThrow(() -> new EntityNotFoundException(characterId));

        new CharacterPathsAndFormsUpdater(request).apply(entity);

        PathsAndFormsEntity saved = repository.save(entity);

        return CharacterPathsAndFormsDtoBuilder.from(saved);
    }
}
