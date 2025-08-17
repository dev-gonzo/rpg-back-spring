package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterBackgroundResponse;
import com.rpgsystem.rpg.domain.entity.BackgroundEntity;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CharacterBackgroundDtoBuilderTest {

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<CharacterBackgroundDtoBuilder> constructor = CharacterBackgroundDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });
    }

    @Test
    void testFromWithValidEntity() {
        // Given
        CharacterEntity character = new CharacterEntity();
        character.setId("char-1");
        
        BackgroundEntity entity = new BackgroundEntity();
        entity.setId("bg-1");
        entity.setCharacter(character);
        entity.setTitle("Test Title");
        entity.setText("Test Text");
        entity.setPublic(true);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        // When
        CharacterBackgroundResponse result = CharacterBackgroundDtoBuilder.from(entity);

        // Then
        assertNotNull(result);
        assertEquals("bg-1", result.getId());
        assertEquals("char-1", result.getCharacterId());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Text", result.getText());
        assertTrue(result.isPublic());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void testFromWithNullEntity() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CharacterBackgroundDtoBuilder.from(null);
        });
    }

    @Test
    void testFromWithPrivateBackground() {
        // Given
        CharacterEntity character = new CharacterEntity();
        character.setId("char-2");
        
        BackgroundEntity entity = new BackgroundEntity();
        entity.setId("bg-2");
        entity.setCharacter(character);
        entity.setTitle("Private Title");
        entity.setText("Private Text");
        entity.setPublic(false);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        // When
        CharacterBackgroundResponse result = CharacterBackgroundDtoBuilder.from(entity);

        // Then
        assertNotNull(result);
        assertEquals("bg-2", result.getId());
        assertEquals("char-2", result.getCharacterId());
        assertEquals("Private Title", result.getTitle());
        assertEquals("Private Text", result.getText());
        assertFalse(result.isPublic());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void testFromWithEmptyStrings() {
        // Given
        CharacterEntity character = new CharacterEntity();
        character.setId("char-3");
        
        BackgroundEntity entity = new BackgroundEntity();
        entity.setId("bg-3");
        entity.setCharacter(character);
        entity.setTitle("");
        entity.setText("");
        entity.setPublic(true);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        // When
        CharacterBackgroundResponse result = CharacterBackgroundDtoBuilder.from(entity);

        // Then
        assertNotNull(result);
        assertEquals("bg-3", result.getId());
        assertEquals("char-3", result.getCharacterId());
        assertEquals("", result.getTitle());
        assertEquals("", result.getText());
        assertTrue(result.isPublic());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void testFromWithNullCharacter() {
        // Given
        BackgroundEntity entity = new BackgroundEntity();
        entity.setId("bg-4");
        entity.setCharacter(null);
        entity.setTitle("Test Title");
        entity.setText("Test Text");
        entity.setPublic(true);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CharacterBackgroundDtoBuilder.from(entity);
        });
    }
}