package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterInfoRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.application.builder.CharacterInfoDtoBuilder;
import com.rpgsystem.rpg.domain.character.CharacterCharacterInfo;
import com.rpgsystem.rpg.domain.common.CodigoId;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterInfoService characterInfoService;
    private final CharacterRepository repository;

    public CharacterEntity getById(String id) {

        CharacterEntity characterEntity = repository.findById(id).orElse(null);

        if (characterEntity == null) {
            throw new EntityNotFoundException(id);
        }

        return characterEntity;

    }

    public CharacterInfoResponse create(CharacterInfoRequest characterInfoRequest, User user) {

        CharacterCharacterInfo characterCharacterInfo = characterInfoService.createInfoDto(characterInfoRequest);
        String id = CodigoId.novo().getValue();

        CharacterEntity character = CharacterEntity.builder()
                .id(id)
                .name(characterCharacterInfo.getName().getValue())
                .profession(characterCharacterInfo.getProfession())
                .birthDate(characterCharacterInfo.getBirthDate())
                .birthPlace(characterCharacterInfo.getBirthPlace())
                .gender(characterCharacterInfo.getGender())
                .age(characterCharacterInfo.getAge())
                .apparentAge(characterCharacterInfo.getApparenteAge())
                .heightCm(characterCharacterInfo.getHeightCm() != null ? characterCharacterInfo.getHeightCm().getCentimeters() : null)
                .weightKg(characterCharacterInfo.getWeightKg() != null ? characterCharacterInfo.getWeightKg().getKilograms() : null)
                .religion(characterCharacterInfo.getReligion())
                .isKnown(false)
                .edit(true)
                .controlUser(user.isMaster() ? null : user)
                .build();
        ;

        repository.save(character);

        return CharacterInfoDtoBuilder.from(character);
    }

}
