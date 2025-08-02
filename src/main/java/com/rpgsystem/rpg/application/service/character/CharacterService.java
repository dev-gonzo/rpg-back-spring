package com.rpgsystem.rpg.application.service.character;


import com.rpgsystem.rpg.application.builder.CharacterInfoDtoBuilder;
import com.rpgsystem.rpg.application.dto.character.CharacterInfoDto;
import com.rpgsystem.rpg.application.dto.character.CharacterInfoRequest;
import com.rpgsystem.rpg.domain.character.CharacterInfo;
import com.rpgsystem.rpg.domain.common.CodigoId;
import com.rpgsystem.rpg.domain.entity.Character;
import com.rpgsystem.rpg.domain.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterInfoService characterInfoService;
    private final CharacterRepository repository;

    public CharacterInfoDto create(CharacterInfoRequest characterInfoRequest) {

        CharacterInfo characterInfo = characterInfoService.createInfoDto(characterInfoRequest);
        String id = CodigoId.novo().getValor();

        Character character = Character.builder()
                .id(id)
                .name(characterInfo.getName().getValue())
                .profession(characterInfo.getProfession())
                .birthDate(characterInfo.getBirthDate())
                .birthPlace(characterInfo.getBirthPlace())
                .gender(characterInfo.getGender())
                .age(characterInfo.getAge())
                .apparentAge(characterInfo.getApparenteAge())
                .heightCm(characterInfo.getHeightCm() != null ? characterInfo.getHeightCm().getCentimeters() : null)
                .weightKg(characterInfo.getWeightKg() != null ? characterInfo.getWeightKg().getKilograms() : null)
                .religion(characterInfo.getReligion())
                .isKnown(false)
                .edit(true)
                .build();;

        repository.save(character);

        return CharacterInfoDtoBuilder.from(character);
    }

}
