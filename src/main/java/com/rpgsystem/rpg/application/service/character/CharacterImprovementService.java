package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterImprovementRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterImprovementResponse;
import com.rpgsystem.rpg.api.dto.character.CharacterPointsResponse;
import com.rpgsystem.rpg.application.builder.CharacterImprovementDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterImprovementUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.ImprovementEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.ImprovementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CharacterImprovementService {

    private final ImprovementRepository improvementRepository;
    private final CharacterAccessValidator characterAccessValidator;
    private final CharacterService characterService;

    private ImprovementEntity getById(String id) {
        ImprovementEntity improvementEntity = improvementRepository.findById(id).orElse(null);

        if (improvementEntity == null) {
            throw new EntityNotFoundException(id);
        }

        return improvementEntity;
    }

    private List<ImprovementEntity> getCharacterImprovements(String characterId) {

        return improvementRepository.findAllByCharacter_Id(characterId);

    }

    public CharacterImprovementResponse getImprovement(String id, String characterId, User user) {
        CharacterEntity characterEntity = characterService.getById(characterId);

        characterAccessValidator.validateControlAccess(characterEntity, user);

        return CharacterImprovementDtoBuilder.from(this.getById(id));

    }

    public List<CharacterImprovementResponse> getImprovements(String characterId, User user) {
        CharacterEntity characterEntity = characterService.getById(characterId);

        characterAccessValidator.validateControlAccess(characterEntity, user);

        List<ImprovementEntity> improvements = getCharacterImprovements(characterId);

        return improvements.stream()
                .map(CharacterImprovementDtoBuilder::from)
                .collect(Collectors.toList());

    }

    public CharacterImprovementResponse save(CharacterImprovementRequest request, String id, String characterId, User user) {
        CharacterEntity characterEntity = characterService.getById(characterId);

        characterAccessValidator.validateControlAccess(characterEntity, user);

        ImprovementEntity improvementEntity = this.getById(id);

        new CharacterImprovementUpdater(request).apply(improvementEntity);

        ImprovementEntity saved = improvementRepository.save(improvementEntity);

        return CharacterImprovementDtoBuilder.from(saved);

    }
}
