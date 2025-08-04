package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.PathsAndFormsRequest;
import com.rpgsystem.rpg.api.dto.character.PathsAndFormsResponse;
import com.rpgsystem.rpg.application.builder.PathsAndFormsDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.PathsAndFormsUpdater;
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

    public PathsAndFormsResponse get(String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        PathsAndFormsEntity entity = repository.findById(characterId)
                .orElseThrow(() -> new EntityNotFoundException(characterId));

        return PathsAndFormsDtoBuilder.from(entity);
    }

    public PathsAndFormsResponse save(String characterId, PathsAndFormsRequest request, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        PathsAndFormsEntity entity = repository.findById(characterId)
                .orElseThrow(() -> new EntityNotFoundException(characterId));

        new PathsAndFormsUpdater(request).apply(entity);

        PathsAndFormsEntity saved = repository.save(entity);

        return PathsAndFormsDtoBuilder.from(saved);
    }
}
