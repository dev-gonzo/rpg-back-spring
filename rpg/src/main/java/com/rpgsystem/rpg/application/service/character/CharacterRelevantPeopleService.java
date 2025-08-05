package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleResponse;
import com.rpgsystem.rpg.application.builder.CharacterRelevantPeopleDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterRelevantPeopleUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.RelevantPeopleEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.RelevantPeopleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterRelevantPeopleService {

    private final RelevantPeopleRepository repository;
    private final CharacterService characterService;
    private final CharacterAccessValidator accessValidator;

    private RelevantPeopleEntity getById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<CharacterRelevantPeopleResponse> getAll(String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        return repository.findAllByCharacter_Id(characterId).stream()
                .map(CharacterRelevantPeopleDtoBuilder::from)
                .collect(Collectors.toList());
    }

    public CharacterRelevantPeopleResponse get(String characterId, String id, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        return CharacterRelevantPeopleDtoBuilder.from(getById(id));
    }

    public CharacterRelevantPeopleResponse save(String characterId, String id, CharacterRelevantPeopleRequest request, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        RelevantPeopleEntity entity = getById(id);
        new CharacterRelevantPeopleUpdater(request).apply(entity);

        RelevantPeopleEntity saved = repository.save(entity);
        return CharacterRelevantPeopleDtoBuilder.from(saved);
    }
}
