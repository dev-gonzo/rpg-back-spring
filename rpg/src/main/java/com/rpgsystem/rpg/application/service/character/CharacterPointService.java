package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.*;
import com.rpgsystem.rpg.application.builder.CharacterPointsDtoBuilder;
import com.rpgsystem.rpg.domain.character.CharacterPoints;
import com.rpgsystem.rpg.domain.character.updater.CharacterCurrentPatchPointsUpdater;
import com.rpgsystem.rpg.domain.character.updater.CharacterCurrentPointsUpdater;
import com.rpgsystem.rpg.domain.character.updater.CharacterPointsUpdater;
import com.rpgsystem.rpg.domain.character.valueObject.Point;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.exception.UnauthorizedActionException;
import com.rpgsystem.rpg.domain.repository.character.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterPointService {

    private final CharacterRepository repository;
    private final CharacterAccessValidator characterAccessValidator;
    private final CharacterService characterService;


    public CharacterPointsResponse getInfo(String id) {

        return CharacterPointsDtoBuilder.from(characterService.getById(id));

    }

    public CharacterPoints createPointsDto(CharacterPointsRequest request) {
        return CharacterPoints.fromBasePoints(
                null,
                Point.of(request.getHitPoints()),
                Point.of(request.getInitiative()),
                Point.of(request.getHeroPoints()),
                Point.of(request.getMagicPoints()),
                Point.of(request.getFaithPoints()),
                Point.of(request.getProtectionIndex())
        );
    }


    public CharacterPoints createPointsDto(CharacterCurrentPointsRequest request) {
        return CharacterPoints.fromCurrentPoints(
                null,
                Point.of(request.getCurrentHitPoints()),
                Point.of(request.getCurrentInitiative()),
                Point.of(request.getCurrentHeroPoints()),
                Point.of(request.getCurrentMagicPoints()),
                Point.of(request.getCurrentFaithPoints()),
                Point.of(request.getCurrentProtectionIndex())
        );
    }


    public CharacterPointsResponse save(CharacterPointsRequest request, String id, User user) {
        return getCharacterPointsResponse(id, user, this.createPointsDto(request), false);
    }

    public CharacterPointsResponse saveCurrent(CharacterCurrentPointsRequest request, String id, User user) {
        return getCharacterPointsResponse(id, user, this.createPointsDto(request), true);
    }

    public CharacterPointsResponse saveCurrentAdjust(CharacterCurrentPointsRequest request, String id, User user) {
        CharacterEntity characterEntity = characterService.getById(id);

        if (!characterEntity.getControlUser().equals(user) && !user.isMaster()) {
            throw new UnauthorizedActionException("Action not performed, user without permission");
        }

        new CharacterCurrentPatchPointsUpdater(request).apply(characterEntity);

        repository.save(characterEntity);

        return CharacterPointsDtoBuilder.from(characterEntity);
    }



    private CharacterPointsResponse getCharacterPointsResponse(String id, User user, CharacterPoints pointsDto, boolean isCurrent) {
        CharacterEntity characterEntity = characterService.getById(id);

        characterAccessValidator.validateControlAccess(characterEntity, user);

        if (isCurrent) {
            new CharacterCurrentPointsUpdater(pointsDto).apply(characterEntity);
        } else {
            new CharacterPointsUpdater(pointsDto).apply(characterEntity);
        }

        repository.save(characterEntity);

        return CharacterPointsDtoBuilder.from(characterEntity);
    }


}
