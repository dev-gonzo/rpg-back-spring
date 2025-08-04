package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterInfoRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.application.builder.CharacterInfoDtoBuilder;
import com.rpgsystem.rpg.domain.character.CharacterInfo;
import com.rpgsystem.rpg.domain.character.updater.CharacterInfoUpdater;
import com.rpgsystem.rpg.domain.character.valueObject.CharacterName;
import com.rpgsystem.rpg.domain.character.valueObject.Height;
import com.rpgsystem.rpg.domain.character.valueObject.Weight;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.exception.UnauthorizedActionException;
import com.rpgsystem.rpg.domain.repository.character.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterInfoService {

    private final CharacterRepository repository;
    private final CharacterAccessValidator characterAccessValidator;
    private final CharacterService characterService;

    public CharacterInfoResponse getInfo(String id) {

        return CharacterInfoDtoBuilder.from(characterService.getById(id));

    }

    public CharacterInfo createInfoDto(CharacterInfoRequest request) {
        return new CharacterInfo(
                null,
                CharacterName.of(request.getName()),
                request.getProfession(),
                request.getBirthDate(),
                request.getBirthPlace(),
                request.getGender(),
                request.getAge(),
                request.getApparentAge(),
                Height.of(request.getHeightCm()),
                Weight.of(request.getWeightKg()),
                request.getReligion()
        );

    }

    public CharacterInfoResponse save(CharacterInfoRequest request, String id, User user) {
        CharacterEntity characterEntity = characterService.getById(id);

        characterAccessValidator.validateControlAccess(characterEntity, user);

        CharacterInfo characterInfo = this.createInfoDto(request);
        CharacterInfoUpdater updater = new CharacterInfoUpdater(characterInfo);
        updater.apply(characterEntity);

        repository.save(characterEntity);

        return CharacterInfoDtoBuilder.from(characterEntity);

    }
}
