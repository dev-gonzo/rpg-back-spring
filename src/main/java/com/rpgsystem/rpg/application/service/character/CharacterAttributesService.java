package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterAttributeModRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterAttributeRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterAttributeResponse;
import com.rpgsystem.rpg.application.builder.CharacterAttributeDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterAttributesModUpdater;
import com.rpgsystem.rpg.domain.character.updater.CharacterAttributesUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.AttributeEntity;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.AttributeRepository;
import com.rpgsystem.rpg.domain.repository.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterAttributesService {

    private final AttributeRepository attributeRepository;
    private final CharacterRepository characterRepository;
    private final CharacterAccessValidator characterAccessValidator;


    private AttributeEntity getOrCreateAttributeEntity(String characterId) {
        AttributeEntity attributeEntity = attributeRepository.findByCharacter_Id(characterId).orElse(null);

        if (attributeEntity == null) {
            CharacterEntity character = characterRepository.findById(characterId)
                    .orElseThrow(() -> new EntityNotFoundException("Character not found: " + characterId));

            attributeEntity = AttributeEntity.builder()
                    .id(java.util.UUID.randomUUID().toString())
                    .character(character)
                    .build();
        }

        return attributeEntity;
    }

    public CharacterAttributeResponse getCharacterAttributes(String characterId) {
        AttributeEntity attributeEntity = attributeRepository.findByCharacter_Id(characterId).orElse(null);

        if (attributeEntity == null) {
            throw new EntityNotFoundException(characterId);
        }

        return CharacterAttributeDtoBuilder.from(attributeEntity);
    }

    public CharacterAttributeResponse save(CharacterAttributeRequest request, String characterId, User user) {
        AttributeEntity attributeEntity = this.getOrCreateAttributeEntity(characterId);

        characterAccessValidator.validateControlAccess(attributeEntity.getCharacter(), user);

        new CharacterAttributesUpdater(request).apply(attributeEntity);

        AttributeEntity saved = attributeRepository.save(attributeEntity);

        return CharacterAttributeDtoBuilder.from(saved);
    }


    public CharacterAttributeResponse saveMod(CharacterAttributeModRequest request, String characterId, User user) {
        AttributeEntity attributeEntity = this.getOrCreateAttributeEntity(characterId);

        characterAccessValidator.validateControlAccess(attributeEntity.getCharacter(), user);

        new CharacterAttributesModUpdater(request).apply(attributeEntity);

        AttributeEntity saved = attributeRepository.save(attributeEntity);

        return CharacterAttributeDtoBuilder.from(saved);
    }

}