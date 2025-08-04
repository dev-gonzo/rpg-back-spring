package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.application.builder.CharacterHomeDtoBuilder;
import com.rpgsystem.rpg.api.dto.character.CharacterHomeDto;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterHomeService {

    private final CharacterRepository repository;

    public List<CharacterHomeDto> listByUser(User user) {

        List<CharacterHomeDto> characters = new ArrayList<>();

        if (user.isMaster()) {
            characters.addAll(
                    repository.findAllPrivateControlledByOthersOrdered(user)
                            .stream().map(CharacterHomeDtoBuilder::from)
                            .collect(Collectors.toList()));

            characters.addAll(repository.findAllByNotControlUserOrdered()
                    .stream().map(CharacterHomeDtoBuilder::from)
                    .collect(Collectors.toList()));
        } else {

            characters.addAll(
                    repository.findAllByControlUserOrdered(user)
                            .stream().map(CharacterHomeDtoBuilder::from)
                            .collect(Collectors.toList()));

            characters.addAll(
                    repository.findAllPrivateControlledByOthersOrdered(user)
                            .stream().map(CharacterHomeDtoBuilder::from)
                            .collect(Collectors.toList()));

            characters.addAll(
                    repository.findAllByControlUserIsNullAndIsKnownTrueOrdered()
                            .stream().map(CharacterHomeDtoBuilder::from)
                            .collect(Collectors.toList()));
        }

        return characters;
    }
}
