package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.application.builder.CharacterInfoDtoBuilder;
import com.rpgsystem.rpg.application.dto.character.CharacterInfoDto;
import com.rpgsystem.rpg.application.dto.character.CharacterInfoRequest;
import com.rpgsystem.rpg.domain.character.CharacterInfo;
import com.rpgsystem.rpg.domain.character.valueobject.CharacterName;
import com.rpgsystem.rpg.domain.character.valueobject.Height;
import com.rpgsystem.rpg.domain.character.valueobject.Weight;
import com.rpgsystem.rpg.domain.common.CodigoId;
import com.rpgsystem.rpg.domain.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterInfoService {

    private final CharacterRepository repository;

    public CharacterInfoDto getById(String id) {

        return repository.findById(id).map(CharacterInfoDtoBuilder::from).orElse(null);

    }

    public CharacterInfo createInfoDto(CharacterInfoRequest request) {
        return new CharacterInfo(
                new CharacterName(request.name()),
                request.profession(),
                request.birthDate(),
                request.birthPlace(),
                request.gender(),
                request.age(),
                request.apparentAge(),
                request.heightCm() != null ? new Height(request.heightCm()) : null,
                request.weightKg() != null ? new Weight(request.weightKg()) : null,
                request.religion()
        );

    }
}
