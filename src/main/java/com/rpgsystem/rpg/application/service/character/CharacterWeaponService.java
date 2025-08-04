package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterWeaponRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterWeaponResponse;
import com.rpgsystem.rpg.application.builder.CharacterWeaponDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterWeaponUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.entity.WeaponEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterWeaponService {

    private final com.rpgsystem.rpg.domain.repository.character.WeaponRepository repository;
    private final CharacterService characterService;
    private final CharacterAccessValidator accessValidator;

    private WeaponEntity getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<CharacterWeaponResponse> getAll(String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        return repository.findAllByCharacter_Id(characterId).stream()
                .map(CharacterWeaponDtoBuilder::from)
                .collect(Collectors.toList());
    }

    public CharacterWeaponResponse get(String characterId, String id, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        return CharacterWeaponDtoBuilder.from(getById(id));
    }

    public CharacterWeaponResponse save(String characterId, String id, CharacterWeaponRequest request, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        WeaponEntity entity = getById(id);
        new CharacterWeaponUpdater(request).apply(entity);

        WeaponEntity saved = repository.save(entity);
        return CharacterWeaponDtoBuilder.from(saved);
    }
}
