package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerResponse;
import com.rpgsystem.rpg.application.builder.CharacterRitualPowerDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterRitualPowerUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.RitualPowerEntity;
import com.rpgsystem.rpg.domain.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterRitualPowerService {

    private final com.rpgsystem.rpg.domain.repository.character.RitualPowerRepository repository;
    private final CharacterService characterService;
    private final CharacterAccessValidator accessValidator;

    private RitualPowerEntity getById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<CharacterRitualPowerResponse> getAll(String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        return repository.findAllByCharacter_Id(characterId).stream()
                .map(CharacterRitualPowerDtoBuilder::from)
                .collect(Collectors.toList());
    }

    public CharacterRitualPowerResponse get(String characterId, String id, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        return CharacterRitualPowerDtoBuilder.from(getById(id));
    }

    public CharacterRitualPowerResponse save(String characterId, String id, CharacterRitualPowerRequest request, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        RitualPowerEntity entity = getById(id);
        new CharacterRitualPowerUpdater(request).apply(entity);

        RitualPowerEntity saved = repository.save(entity);
        return CharacterRitualPowerDtoBuilder.from(saved);
    }
}
