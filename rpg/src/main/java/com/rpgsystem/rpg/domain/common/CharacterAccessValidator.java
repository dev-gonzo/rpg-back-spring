package com.rpgsystem.rpg.domain.common;

import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CharacterAccessValidator {

    public void validateControlAccess(CharacterEntity character, User user) {
        if (user == null) {
            throw new NullPointerException("User cannot be null");
        }
        
        if (character == null) {
            throw new EntityNotFoundException("Character not found");
        }
        
        // Master users always have access
        if (user.isMaster()) {
            return;
        }
        
        // Check if user has control access
        String userId = user.getId();
        String controlUserId = character.getControlUser() != null ? character.getControlUser().getId() : null;
        
        if (userId != null && userId.equals(controlUserId)) {
            return;
        }
        
        throw new SecurityException("User does not have permission to modify this character.");
    }
}

