package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterEquipmentRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterEquipmentResponse;
import com.rpgsystem.rpg.application.builder.CharacterEquipmentDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterEquipmentUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.EquipmentEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.EquipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterEquipmentService {

    private final EquipmentRepository repository;
    private final CharacterService characterService;
    private final CharacterAccessValidator accessValidator;

    private EquipmentEntity getById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<CharacterEquipmentResponse> getAll(String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        return repository.findAllByCharacter_Id(characterId).stream()
                .map(CharacterEquipmentDtoBuilder::from)
                .collect(Collectors.toList());
    }

    public CharacterEquipmentResponse get(String characterId, String id, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        return CharacterEquipmentDtoBuilder.from(getById(id));
    }

    public CharacterEquipmentResponse save(String characterId, String id, CharacterEquipmentRequest request, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        EquipmentEntity entity = getById(id);
        new CharacterEquipmentUpdater(request).apply(entity);

        EquipmentEntity saved = repository.save(entity);
        return CharacterEquipmentDtoBuilder.from(saved);
    }
}
