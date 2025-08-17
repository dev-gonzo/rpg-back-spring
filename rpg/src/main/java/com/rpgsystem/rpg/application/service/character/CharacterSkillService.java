package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterSkillRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterSkillResponse;
import com.rpgsystem.rpg.application.builder.CharacterSkillDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterSkillUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.SkillEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterSkillService {

    private final SkillRepository skillRepository;
    private final CharacterAccessValidator characterAccessValidator;
    private final CharacterService characterService;

    private SkillEntity getById(String id) {
        return skillRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    private List<SkillEntity> getCharacterSkills(String characterId) {
        return skillRepository.findAllByCharacter_Id(characterId);
    }

    public CharacterSkillResponse getSkill(String id, String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        characterAccessValidator.validateControlAccess(character, user);

        return CharacterSkillDtoBuilder.build(this.getById(id));
    }

    public List<CharacterSkillResponse> getSkills(String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        characterAccessValidator.validateControlAccess(character, user);

        return getCharacterSkills(characterId).stream()
                .map(CharacterSkillDtoBuilder::build)
                .collect(Collectors.toList());
    }

    public CharacterSkillResponse save(CharacterSkillRequest request, String id, String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        characterAccessValidator.validateControlAccess(character, user);

        SkillEntity skill = this.getById(id);
        new CharacterSkillUpdater(request).apply(skill);

        SkillEntity saved = skillRepository.save(skill);
        return CharacterSkillDtoBuilder.build(saved);
    }
}
