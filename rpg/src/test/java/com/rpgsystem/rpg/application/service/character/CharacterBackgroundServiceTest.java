package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterBackgroundRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterBackgroundResponse;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.BackgroundEntity;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.BackgroundRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterBackgroundServiceTest {

    @Mock
    private BackgroundRepository repository;

    @Mock
    private CharacterService characterService;

    @Mock
    private CharacterAccessValidator accessValidator;

    @InjectMocks
    private CharacterBackgroundService service;

    private User user;
    private CharacterEntity character;
    private BackgroundEntity backgroundEntity;
    private CharacterBackgroundRequest request;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("user-1")
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .isMaster(false)
                .build();

        character = CharacterEntity.builder()
                .id("character-1")
                .name("Test Character")
                .build();

        backgroundEntity = BackgroundEntity.builder()
                .id("background-1")
                .character(character)
                .title("Character Background")
                .text("This is a detailed character background story.")
                .isPublic(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        request = CharacterBackgroundRequest.builder()
                .title("Updated Background")
                .text("This is an updated character background story.")
                .isPublic(false)
                .build();
    }

    @Test
    void get_ShouldReturnCharacterBackgroundResponse_WhenBackgroundExists() {
        // Given
        when(characterService.getById("character-1")).thenReturn(character);
        when(repository.findByCharacter_Id("character-1")).thenReturn(Optional.of(backgroundEntity));

        // When
        CharacterBackgroundResponse result = service.get("character-1", user);

        // Then
        assertNotNull(result);
        assertEquals("background-1", result.getId());
        assertEquals("character-1", result.getCharacterId());
        assertEquals("Character Background", result.getTitle());
        assertEquals("This is a detailed character background story.", result.getText());
        assertTrue(result.isPublic());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(characterService).getById("character-1");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findByCharacter_Id("character-1");
    }

    @Test
    void get_ShouldThrowEntityNotFoundException_WhenBackgroundNotFound() {
        // Given
        when(characterService.getById("character-1")).thenReturn(character);
        when(repository.findByCharacter_Id("character-1")).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.get("character-1", user)
        );

        assertEquals("Background not found for character character-1", exception.getMessage());
        verify(characterService).getById("character-1");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findByCharacter_Id("character-1");
    }

    @Test
    void get_ShouldThrowException_WhenCharacterNotFound() {
        // Given
        when(characterService.getById("character-1")).thenThrow(new EntityNotFoundException("Character not found"));

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.get("character-1", user)
        );

        assertEquals("Character not found", exception.getMessage());
        verify(characterService).getById("character-1");
        verifyNoInteractions(accessValidator);
        verifyNoInteractions(repository);
    }

    @Test
    void save_ShouldReturnUpdatedCharacterBackgroundResponse_WhenSuccessful() {
        // Given
        BackgroundEntity updatedEntity = BackgroundEntity.builder()
                .id("background-1")
                .character(character)
                .title("Updated Background")
                .text("This is an updated character background story.")
                .isPublic(false)
                .createdAt(backgroundEntity.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        when(characterService.getById("character-1")).thenReturn(character);
        when(repository.findById("background-1")).thenReturn(Optional.of(backgroundEntity));
        when(repository.save(any(BackgroundEntity.class))).thenReturn(updatedEntity);

        // When
        CharacterBackgroundResponse result = service.save("character-1", "background-1", request, user);

        // Then
        assertNotNull(result);
        assertEquals("background-1", result.getId());
        assertEquals("character-1", result.getCharacterId());
        assertEquals("Updated Background", result.getTitle());
        assertEquals("This is an updated character background story.", result.getText());
        assertFalse(result.isPublic());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(characterService).getById("character-1");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById("background-1");
        verify(repository).save(any(BackgroundEntity.class));
    }

    @Test
    void save_ShouldThrowEntityNotFoundException_WhenBackgroundNotFound() {
        // Given
        when(characterService.getById("character-1")).thenReturn(character);
        when(repository.findById("background-1")).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.save("character-1", "background-1", request, user)
        );

        assertEquals("background-1", exception.getMessage());
        verify(characterService).getById("character-1");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById("background-1");
        verify(repository, never()).save(any(BackgroundEntity.class));
    }

    @Test
    void save_ShouldThrowException_WhenCharacterNotFound() {
        // Given
        when(characterService.getById("character-1")).thenThrow(new EntityNotFoundException("Character not found"));

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.save("character-1", "background-1", request, user)
        );

        assertEquals("Character not found", exception.getMessage());
        verify(characterService).getById("character-1");
        verifyNoInteractions(accessValidator);
        verifyNoInteractions(repository);
    }

    @Test
    void save_ShouldHandleNullRequest_WhenUpdaterReceivesNull() {
        // Given
        when(characterService.getById("character-1")).thenReturn(character);
        when(repository.findById("background-1")).thenReturn(Optional.of(backgroundEntity));
        when(repository.save(any(BackgroundEntity.class))).thenReturn(backgroundEntity);

        // When
        CharacterBackgroundResponse result = service.save("character-1", "background-1", null, user);

        // Then
        assertNotNull(result);
        assertEquals("background-1", result.getId());
        assertEquals("character-1", result.getCharacterId());
        // Original values should remain unchanged when request is null
        assertEquals("Character Background", result.getTitle());
        assertEquals("This is a detailed character background story.", result.getText());
        assertTrue(result.isPublic());

        verify(characterService).getById("character-1");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById("background-1");
        verify(repository).save(any(BackgroundEntity.class));
    }

    @Test
    void save_ShouldHandleEmptyStrings_WhenRequestHasEmptyValues() {
        // Given
        CharacterBackgroundRequest emptyRequest = CharacterBackgroundRequest.builder()
                .title("")
                .text("")
                .isPublic(true)
                .build();

        BackgroundEntity updatedEntity = BackgroundEntity.builder()
                .id("background-1")
                .character(character)
                .title("")
                .text("")
                .isPublic(true)
                .createdAt(backgroundEntity.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        when(characterService.getById("character-1")).thenReturn(character);
        when(repository.findById("background-1")).thenReturn(Optional.of(backgroundEntity));
        when(repository.save(any(BackgroundEntity.class))).thenReturn(updatedEntity);

        // When
        CharacterBackgroundResponse result = service.save("character-1", "background-1", emptyRequest, user);

        // Then
        assertNotNull(result);
        assertEquals("background-1", result.getId());
        assertEquals("character-1", result.getCharacterId());
        assertEquals("", result.getTitle());
        assertEquals("", result.getText());
        assertTrue(result.isPublic());

        verify(characterService).getById("character-1");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById("background-1");
        verify(repository).save(any(BackgroundEntity.class));
    }

    @Test
    void save_ShouldHandleLongText_WhenRequestHasMaximumValues() {
        // Given
        String longTitle = "A".repeat(255); // Maximum title length
        String longText = "B".repeat(10000); // Long text content
        
        CharacterBackgroundRequest maxRequest = CharacterBackgroundRequest.builder()
                .title(longTitle)
                .text(longText)
                .isPublic(false)
                .build();

        BackgroundEntity updatedEntity = BackgroundEntity.builder()
                .id("background-1")
                .character(character)
                .title(longTitle)
                .text(longText)
                .isPublic(false)
                .createdAt(backgroundEntity.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        when(characterService.getById("character-1")).thenReturn(character);
        when(repository.findById("background-1")).thenReturn(Optional.of(backgroundEntity));
        when(repository.save(any(BackgroundEntity.class))).thenReturn(updatedEntity);

        // When
        CharacterBackgroundResponse result = service.save("character-1", "background-1", maxRequest, user);

        // Then
        assertNotNull(result);
        assertEquals("background-1", result.getId());
        assertEquals("character-1", result.getCharacterId());
        assertEquals(longTitle, result.getTitle());
        assertEquals(longText, result.getText());
        assertFalse(result.isPublic());

        verify(characterService).getById("character-1");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById("background-1");
        verify(repository).save(any(BackgroundEntity.class));
    }
}