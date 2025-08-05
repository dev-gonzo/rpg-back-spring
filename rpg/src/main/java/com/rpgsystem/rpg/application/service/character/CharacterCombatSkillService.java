package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterCombatSkillRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterCombatSkillResponse;
import com.rpgsystem.rpg.application.builder.CombatSkillDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterCombatSkillUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.CombatSkillEntity;
import com.rpgsystem.rpg.domain.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterCombatSkillService {

    private final com.rpgsystem.rpg.domain.repository.character.CombatSkillRepository repository;
    private final CharacterAccessValidator accessValidator;
    private final CharacterService characterService;

    private CombatSkillEntity getById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    private List<CombatSkillEntity> getCharacterSkills(String characterId) {
        return repository.findAllByCharacter_Id(characterId);
    }

    public CharacterCombatSkillResponse getSkill(String id, String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        return CombatSkillDtoBuilder.from(getById(id));
    }

    public List<CharacterCombatSkillResponse> getSkills(String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        return getCharacterSkills(characterId).stream()
                .map(CombatSkillDtoBuilder::from)
                .collect(Collectors.toList());
    }

    public CharacterCombatSkillResponse save(CharacterCombatSkillRequest request, String id, String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        CombatSkillEntity skill = getById(id);
        new CharacterCombatSkillUpdater(request).apply(skill);

        CombatSkillEntity saved = repository.save(skill);
        return CombatSkillDtoBuilder.from(saved);
    }
}
