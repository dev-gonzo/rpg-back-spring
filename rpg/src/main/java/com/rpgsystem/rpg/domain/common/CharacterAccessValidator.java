package com.rpgsystem.rpg.domain.common;

import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CharacterAccessValidator {

    public void validateControlAccess(CharacterEntity character, User user) {
        if (character == null) {
            throw new EntityNotFoundException("Character not found");
        }

        if (!user.isMaster() && (character.getControlUser() == null || !character.getControlUser().getId().equals(user.getId()))) {
            throw new SecurityException("User does not have permission to modify this character.");
        }
    }
}

