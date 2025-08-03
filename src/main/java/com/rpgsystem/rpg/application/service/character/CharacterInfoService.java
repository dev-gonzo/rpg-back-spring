package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterInfoRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.application.builder.CharacterInfoDtoBuilder;
import com.rpgsystem.rpg.domain.character.CharacterInfo;
import com.rpgsystem.rpg.domain.character.updater.CharacterInfoUpdater;
import com.rpgsystem.rpg.domain.character.valueobject.CharacterName;
import com.rpgsystem.rpg.domain.character.valueobject.Height;
import com.rpgsystem.rpg.domain.character.valueobject.Weight;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.exception.UnauthorizedActionException;
import com.rpgsystem.rpg.domain.repository.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterInfoService {

    private final CharacterRepository repository;


    private CharacterEntity getById(String id) {

         CharacterEntity characterEntity = repository.findById(id).orElse(null);

        if(characterEntity == null) {
            throw new EntityNotFoundException(id);
        }

        return characterEntity;

    }

    public CharacterInfoResponse getInfo(String id) {

        return CharacterInfoDtoBuilder.from(this.getById(id));

    }

    public CharacterInfo createInfoDto(CharacterInfoRequest request) {
        return new CharacterInfo(
                null,
                CharacterName.of(request.name()),
                request.profession(),
                request.birthDate(),
                request.birthPlace(),
                request.gender(),
                request.age(),
                request.apparentAge(),
                Height.of(request.heightCm()),
                Weight.of(request.weightKg()),
                request.religion()
        );

    }

    public CharacterInfoResponse save(CharacterInfoRequest request, String id, User user) {
        CharacterEntity characterEntity = this.getById(id);



        if(!characterEntity.getControlUser().equals(user) && !user.isMaster()) {
            throw new UnauthorizedActionException("Action not performed, user without permission");
        }

        CharacterInfo characterInfo = this.createInfoDto(request);
        CharacterInfoUpdater updater = new CharacterInfoUpdater(characterInfo);
        updater.apply(characterEntity);

        repository.save(characterEntity);

        return CharacterInfoDtoBuilder.from(characterEntity);

    }
}
