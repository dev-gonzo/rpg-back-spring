package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterInfoRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.application.builder.CharacterInfoDtoBuilder;
import com.rpgsystem.rpg.application.service.shared.ImageService;
import com.rpgsystem.rpg.domain.character.CharacterCharacterInfo;
import com.rpgsystem.rpg.domain.character.updater.CharacterInfoUpdater;
import com.rpgsystem.rpg.domain.character.valueObject.CharacterName;
import com.rpgsystem.rpg.domain.character.valueObject.Height;
import com.rpgsystem.rpg.domain.character.valueObject.Weight;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.common.CodigoId;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository repository;
    private final ImageService imageService;
    private final CharacterAccessValidator characterAccessValidator;

    public CharacterEntity getById(String id) {

        CharacterEntity characterEntity = repository.findById(id).orElse(null);

        if (characterEntity == null) {
            throw new EntityNotFoundException(id);
        }

        return characterEntity;

    }

    public CharacterInfoResponse getInfo(String id) {

        return CharacterInfoDtoBuilder.from(this.getById(id));

    }

    public CharacterInfoResponse create(CharacterInfoRequest characterInfoRequest, User user) {

        CharacterCharacterInfo characterCharacterInfo = this.createInfoDto(characterInfoRequest);
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

    public CharacterCharacterInfo createInfoDto(CharacterInfoRequest request) {
        return new CharacterCharacterInfo(
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
        CharacterEntity characterEntity = this.getById(id);

        characterAccessValidator.validateControlAccess(characterEntity, user);

        CharacterCharacterInfo characterCharacterInfo = this.createInfoDto(request);
        CharacterInfoUpdater updater = new CharacterInfoUpdater(characterCharacterInfo);
        updater.apply(characterEntity);

        repository.save(characterEntity);

        return CharacterInfoDtoBuilder.from(characterEntity);

    }


    public void uploadImage(String characterId, MultipartFile file, User user) {
        CharacterEntity character = getById(characterId);

        characterAccessValidator.validateControlAccess(character, user);

        String base64Image = imageService.processToBase64(file);
        character.setImage(base64Image);
        repository.save(character);
    }

}
