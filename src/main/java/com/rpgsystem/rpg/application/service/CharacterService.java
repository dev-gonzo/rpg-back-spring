package com.rpgsystem.rpg.application.service;

import com.rpgsystem.rpg.application.builder.CharacterHomeDtoBuilder;
import com.rpgsystem.rpg.application.dto.CharacterHomeDto;
import com.rpgsystem.rpg.domain.model.User;
import com.rpgsystem.rpg.domain.repository.CharacterRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CharacterService {

    private final CharacterRepository repository;

    public CharacterService(CharacterRepository repository) {
        this.repository = repository;
    }

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
