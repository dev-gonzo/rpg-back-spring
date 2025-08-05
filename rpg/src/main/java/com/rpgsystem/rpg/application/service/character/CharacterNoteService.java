package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterNoteRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterNoteResponse;
import com.rpgsystem.rpg.application.builder.CharacterNoteDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterNoteUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.NoteEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.NoteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterNoteService {

    private final NoteRepository repository;
    private final CharacterService characterService;
    private final CharacterAccessValidator accessValidator;

    private NoteEntity getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<CharacterNoteResponse> getAll(String characterId, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        return repository.findAllByCharacter_Id(characterId).stream()
                .map(CharacterNoteDtoBuilder::from)
                .collect(Collectors.toList());
    }

    public CharacterNoteResponse get(String characterId, String id, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        return CharacterNoteDtoBuilder.from(getById(id));
    }

    public CharacterNoteResponse save(String characterId, String id, CharacterNoteRequest request, User user) {
        CharacterEntity character = characterService.getById(characterId);
        accessValidator.validateControlAccess(character, user);

        NoteEntity entity = getById(id);
        new CharacterNoteUpdater(request).apply(entity);

        NoteEntity saved = repository.save(entity);
        return CharacterNoteDtoBuilder.from(saved);
    }
}
